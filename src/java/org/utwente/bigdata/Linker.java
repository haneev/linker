package org.utwente.bigdata;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import org.commoncrawl.examples.ExampleTextWordCount;
import org.commoncrawl.examples.SampleFilter;
import org.commoncrawl.hadoop.mapred.ArcInputFormat;
import org.commoncrawl.hadoop.mapred.ArcRecord;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * An example showing how to analyze the Common Crawl ARC web content files.
 * @author Chris Stephens <chris@commoncrawl.org>
 * 
 * Improved for Linker by
 * @author Han van der Veen
 */
public class Linker extends Configured implements Tool {

  private static final Logger LOG = Logger.getLogger(Linker.class);
  private static final String ARGNAME_INPATH = "-in";
  private static final String ARGNAME_OUTPATH = "-out";
  private static final String ARGNAME_CONF = "-conf";
  private static final String ARGNAME_OVERWRITE = "-overwrite";
  private static final String ARGNAME_COMPRESS = "-compress";
  private static final String ARGNAME_MAXFILES = "-maxfiles";
  private static final String ARGNAME_NUMREDUCE = "-numreducers";
  private static final String FILEFILTER = ".arc.gz";
  
  private static Helper h = new Helper();
  
  protected static enum MAPPERCOUNTER {
    HTML_ERROR,
    EXCEPTIONS,
    OUT_OF_MEMORY,
    DOCUMENTS_IN,
    DURATION_TO_LONG
  };

  /**
   */
  public static class LinkerMapper extends Mapper<Text, ArcRecord, Text, LongWritable> {
      
    private LongWritable outVal = new LongWritable(1);
 
    // HTML to be parsed
    private String html;
    
    public void map(Text key, ArcRecord value, final Context context) throws IOException {
      try {
    	  
    	ExecutorService executor = Executors.newFixedThreadPool(1);
    	  
    	// only get html
        if (!value.getContentType().contains("html")) {
          context.getCounter(MAPPERCOUNTER.HTML_ERROR).increment(1);
          return;
        }

        // ensure sample instances have enough memory to parse HTML
        if (value.getContentLength() > (1 * 1024 * 1024)) {
          context.getCounter(MAPPERCOUNTER.HTML_ERROR).increment(1);
          return;
        }
        
        html = value.getHTML();
        if(html == null) {
       	 	context.getCounter(MAPPERCOUNTER.HTML_ERROR).increment(1);
            return;
        }
        
        // maximum time for executing a page is 200ms
        Future<?> future = executor.submit(new Runnable() {
        	public void run() {
        		
        		try {
        			context.getCounter(MAPPERCOUNTER.DOCUMENTS_IN).increment(1);
        				                	
	                // get a list of tuples of the html
	                List<Tuple<String, String>> tuples = h.parts(html);
	
	                // parse it and emit them concatenated with ||
                	for(Tuple<String, String> tuple : tuples) {
                		String a = tuple.getFirst().trim();
                		String b = tuple.getSecond().trim();
                		if(b.length() > 3 && a.length() > 3) {
                			context.write(new Text(a + "||" + b), outVal);
                		}
                	}
        		} catch(Exception e) {
        			 LOG.error("Caught Exception", e);
        		     context.getCounter(MAPPERCOUNTER.EXCEPTIONS).increment(1);
        		}
        	}
        });
        
        int timeout = 40;
        
        // copied from http://stackoverflow.com/questions/20500003/setting-a-maximum-execution-time-for-a-method-thread
        executor.shutdown();
        try {
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        	LOG.error("Caught Exception", e);
            context.getCounter(MAPPERCOUNTER.EXCEPTIONS).increment(1);
        } catch (TimeoutException e) {
            future.cancel(true);
            context.getCounter(MAPPERCOUNTER.DURATION_TO_LONG).increment(1);
        }

        if(!executor.awaitTermination(timeout+2, TimeUnit.MILLISECONDS)){
            executor.shutdownNow();
        }
        
      }
      catch (Throwable e) {
        LOG.error("Caught Exception", e);
        context.getCounter(MAPPERCOUNTER.EXCEPTIONS).increment(1);
      }
    }
  }
  
  public void usage() {
    System.out.println("\n  org.commoncrawl.examples.ExampleArcMicroformat \n" +
                         "                           " + ARGNAME_INPATH +" <inputpath>\n" +
                         "                           " + ARGNAME_OUTPATH + " <outputpath>\n" +
                         "                         [ " + ARGNAME_OVERWRITE + " ]\n" +
                         "                         [ " + ARGNAME_NUMREDUCE + " <number_of_reducers> ]\n" +
                         "                         [ " + ARGNAME_CONF + " <conffile> ]\n" +
                         "                         [ " + ARGNAME_MAXFILES + " <maxfiles> ]");
    System.out.println("");
    GenericOptionsParser.printGenericCommandUsage(System.out);
  }

  /**
   * Implmentation of Tool.run() method, which builds and runs the Hadoop job.
   *
   * @param  args command line parameters, less common Hadoop job parameters stripped
   *              out and interpreted by the Tool class.  
   * @return      0 if the Hadoop job completes successfully, 1 if not. 
   */
  @Override
  public int run(String[] args) throws Exception {

    String inputPath = null;
    String outputPath = null;
    String configFile = null;
    
    boolean overwrite = false;
    boolean compression = false;
    
    int numReducers = 1;

    // Read the command line arguments. We're not using GenericOptionsParser
    // to prevent having to include commons.cli as a dependency.
    for (int i = 0; i < args.length; i++) {
      try {
        if (args[i].equals(ARGNAME_INPATH)) {
          inputPath = args[++i];
        } else if (args[i].equals(ARGNAME_OUTPATH)) {
          outputPath = args[++i];
        } else if (args[i].equals(ARGNAME_CONF)) {
          configFile = args[++i];
        } else if (args[i].equals(ARGNAME_MAXFILES)) {
          SampleFilter.setMax(Long.parseLong(args[++i]));
        } else if (args[i].equals(ARGNAME_OVERWRITE)) {
          overwrite = true;
        } else if (args[i].equals(ARGNAME_COMPRESS)) {
          compression = true;
        } else if (args[i].equals(ARGNAME_NUMREDUCE)) {
          numReducers = Integer.parseInt(args[++i]);
        } else {
          LOG.warn("Unsupported argument: " + args[i]);
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        usage();
        throw new IllegalArgumentException();
      }
    }
    
    if (inputPath == null || outputPath == null) {
      usage();
      throw new IllegalArgumentException();
    }

    // Read in any additional config parameters.
    if (configFile != null) {
      LOG.info("adding config parameters from '"+ configFile + "'");
      this.getConf().addResource(configFile);
    }

    // Create the Hadoop job.
    Configuration conf = getConf();
    conf.set("mapred.textoutputformat.separator",";");
    conf.set("mapred.task.timeout", "750000");
    
    Job job = new Job(conf);
    job.setJarByClass(ExampleTextWordCount.class);
    job.setNumReduceTasks(numReducers);

    // Scan the provided input path for ARC files.
    LOG.info("setting input path to '"+ inputPath + "'");
    SampleFilter.setFilter(FILEFILTER);
    FileInputFormat.addInputPath(job, new Path(inputPath));
    FileInputFormat.setInputPathFilter(job, SampleFilter.class);

    // Delete the output path directory if it already exists and user wants to overwrite it.
    if (overwrite) {
      LOG.info("clearing the output path at '" + outputPath + "'");
      FileSystem fs = FileSystem.get(new URI(outputPath), conf);
      if (fs.exists(new Path(outputPath))) {
        fs.delete(new Path(outputPath), true);
      }
    }
    
    // Set the path where final output 'part' files will be saved.
    LOG.info("setting output path to '" + outputPath + "'");
    FileOutputFormat.setOutputPath(job, new Path(outputPath));
    
    if(compression) {
    	FileOutputFormat.setCompressOutput(job, true);
    	FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class); // makes live very easy
    }

    // Set which InputFormat class to use.
    job.setInputFormatClass(ArcInputFormat.class);

    // Set which OutputFormat class to use.
    job.setOutputFormatClass(TextOutputFormat.class);
 
    // Set the output data types.
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(LongWritable.class);
    
    job.setJobName("UTwente - Managing Big Data - Linker");

    job.setMapperClass(Linker.LinkerMapper.class);
    job.setReducerClass(LongSumReducer.class);
    job.setCombinerClass(LongSumReducer.class);

    return job.waitForCompletion(true) ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(), new Linker(), args);
    System.exit(res);
  }
}
