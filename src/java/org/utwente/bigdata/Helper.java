package org.utwente.bigdata;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class that only parse the sentences
 * @author Han van der Veen
 */
public class Helper {
	/**
	 * Pattern that matches the lists: a,b,c or z. 
	 */
    public static Pattern p = Pattern.compile("([a-z0-9\\-\\040]{3,20}[,])([a-z0-9_\\-\\040]{3,40}[,]\\s*){0,8}([a-z0-9_\\-\\040]{3,40}(\\s+(and|or)\\s+)[a-z0-9_\\-\\040]{3,40})", Pattern.CASE_INSENSITIVE);
	
    /**
     * Parse the lists into Tuples
     * @param html
     * @return list of tuples
     */
    public List<Tuple<String, String>> parts(String html) {
		// strip html
		html = html.replaceAll("<[^>]+>","");
		
    	List<Tuple<String,String>> list = new ArrayList<Tuple<String,String>>();
    	Matcher m = p.matcher(html);
    	
    	while(m.find()) {
    		
    		// items of the lists
    		List<String> items = new ArrayList<String>(); 
    		
    		String all = m.group(0);
    		String[] parts = all.split("([,;]|\\Wand\\W|\\Wor\\W)");
    		
    		// item to a lists
    		for(String part : parts) {
				part = part.trim();
    			if(part.length() > 2)
    				items.add(part.toLowerCase());
    		}
    	
    		// Parse the combinations into Tuples. Only tuples with (a,b) and not (b,a) and only if a is not already parsed.
    		List<String> parsed = new ArrayList<String>();
    		for(String item : items) {
    			for(String inner : items) {
    				if(!item.equals(inner)) {
    					String a,b;
    					if(item.compareTo(inner) < 0) {
    						a = item; b = inner;
    					} else {
    						b = item; a = inner;
    					}
    					
    					// check if tuple already exists...
    					if(!parsed.contains(a)) {
    						Tuple<String, String> t = new Tuple<String, String>(a,b);    						    						
    						list.add(t);
    						parsed.add(a);
    					}
    				}
    			}
    		}
    	}
    	
    	return list;
    }
}
