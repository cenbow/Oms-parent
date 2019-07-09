package com.work.shop.oms.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.work.shop.oms.common.bean.ConstantValues;



public class StringUtil extends StringUtils{
	
	
    /**
     * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
     */
	private static String[] strFilterArr={"'"};
	
    private StringUtil() {
    }
    

    /**
     * An empty immutable <code>String</code> array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * 回车并换行，为替换准备
     */
    public static final String RETURN_AND_NEWLINE = "RETURN_AND_NEWLINE";


    /**
     * 循环打印字符串数组。
     * 字符串数组的各元素间以指定字符分隔，如果字符串中已经包含指定字符则在字符串的两端加上双引号。
     * @param strings 字符串数组
     * @param delim 分隔符
     * @param out 打印到的输出流
     */
    public static void printStrings(String[] strings, String delim,
                                    OutputStream out) {
        try {
            if (strings != null) {
                int length = strings.length - 1;
                if (length > -1) {
                    for (int i = 0; i < length; i++) {
                        if (strings[i] != null) {
//			  System.out.println("strings[i]:"+strings[i]);
//			  System.out.println("delim:"+delim);
                            if (strings[i].indexOf(delim) > -1) {
                                out.write(("\"" + strings[i] + "\"" + delim).
                                          getBytes());
                            } else {
                                out.write((strings[i] + delim).getBytes());
                            }
                        } else {
                            out.write("null".getBytes());
                        }
                    }

                    if (strings[length] != null) {

                        if (strings[length].indexOf(delim) > -1) {
                            out.write(("\"" + strings[length] + "\"").getBytes());

                        } else {
                            out.write(strings[length].getBytes());
                        }
                    } else {
                        out.write("null".getBytes());
                    }
                } else {
                    out.write("null".getBytes());
                }
                out.write("\n".getBytes());
            }
        } catch (IOException e) {

        }
    }

    /**
     * 循环打印字符串数组到标准输出。
     * 字符串数组的各元素间以指定字符分隔，如果字符串中已经包含指定字符则在字符串的两端加上双引号。
     * @param strings 字符串数组
     * @param delim 分隔符
     */
    public static void printStrings(String[] strings, String delim) {
        printStrings(strings, delim, System.out);
    }

    /**
     * 循环打印字符串数组。
     * 字符串数组的各元素间以逗号分隔，如果字符串中已经包含逗号则在字符串的两端加上双引号。
     * @param strings 字符串数组
     * @param out 打印到的输出流
     */
    public static void printStrings(String[] strings, OutputStream out) {
        printStrings(strings, ",", out);
    }

    /**
     * 循环打印字符串数组到系统标准输出流System.out。
     * 字符串数组的各元素间以逗号分隔，如果字符串中已经包含逗号则在字符串的两端加上双引号。
     * @param strings 字符串数组
     * @since  0.2
     */
    public static void printStrings(String[] strings) {
        printStrings(strings, ",", System.out);
    }


    /**
     * 字符串数组中是否包含指定的字符串。
     * @param strings 字符串数组
     * @param string 字符串
     * @param caseSensitive 是否大小写敏感
     * @return 包含时返回true，否则返回false
     * @since  0.4
     */
    public static boolean contains(String[] strings, String string,
                                   boolean caseSensitive) {
        for (int i = 0; i < strings.length; i++) {
            if (caseSensitive == true) {
                if (strings[i].equals(string)) {
                    return true;
                }
            } else {
                if (strings[i].equalsIgnoreCase(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 字符串数组中是否包含指定的字符串。大小写敏感。
     * @param strings 字符串数组
     * @param string 字符串
     * @return 包含时返回true，否则返回false
     * @since  0.4
     */
    public static boolean contains(String[] strings, String string) {
        return contains(strings, string, true);
    }

    /**
     * 不区分大小写判定字符串数组中是否包含指定的字符串。
     * @param strings 字符串数组
     * @param string 字符串
     * @return 包含时返回true，否则返回false
     */
    public static boolean containsIgnoreCase(String[] strings, String string) {
        return contains(strings, string, false);
    }

    /**
     * 将字符串数组使用指定的分隔符合并成一个字符串。
     * @param array 字符串数组
     * @param delim 分隔符，为null的时候使用""作为分隔符（即没有分隔符）
     * @return 合并后的字符串
     * @since  0.4
     */
    public static String combineStringArray(String[] array, String delim) {
        int length = array.length - 1;
        if (delim == null) {
            delim = "";
        }
        StringBuffer result = new StringBuffer(length * 8);
        for (int i = 0; i < length; i++) {
            result.append(array[i]);
            result.append(delim);
        }
        result.append(array[length]);
        return result.toString();
    }


    /**
     * 去除左边多余的空格。
     * @param value 待去左边空格的字符串
     * @return 去掉左边空格后的字符串
     * @since  0.6
     */
    public static String trimLeft(String value) {
        String result = value;
        if (result == null) {
            return result;
        }
        char ch[] = result.toCharArray();
        int index = -1;
        for (int i = 0; i < ch.length; i++) {
            if (Character.isWhitespace(ch[i])) {
                index = i;
            } else {
                break;
            }
        }
        if (index != -1) {
            result = result.substring(index + 1);
        }
        return result;
    }

    /**
     * 去除右边多余的空格。
     * @param value 待去右边空格的字符串
     * @return 去掉右边空格后的字符串
     * @since  0.6
     */
    public static String trimRight(String value) {
        String result = value;
        if (result == null) {
            return result;
        }
        char ch[] = result.toCharArray();
        int endIndex = -1;
        for (int i = ch.length - 1; i > -1; i--) {
            if (Character.isWhitespace(ch[i])) {
                endIndex = i;
            } else {
                break;
            }
        }
        if (endIndex != -1) {
            result = result.substring(0, endIndex);
        }
        return result;
    }

    /**
     * 替换String中的字符串.
     * 在标准的String类中只有将String中的某一个字符替换成另一个字符，该函数可以将String
     * 中所有的finder字符串替换成replacement字符串.
     * @param source 源字符串
     * @param finder 将要被替换的字符串,如果finder为空或null，则默认为空格
     * @param replacement 将要替换的字符串
     * @return 将源字符串替换后的字符串
     */
    public static String replaceAll(String source,
                                    String finder,
                                    String replacement) {
        if (source == null || source.equals("")) {
            return source;
        }
        if (finder == null || finder.equals("")) {
            finder = " ";
        }
        String str0 = source;
        String target = "";
        try {
            if (finder == StringUtil.RETURN_AND_NEWLINE) {
                while (str0.indexOf("\r") != -1) {
                    target += str0.substring(0, str0.indexOf("\r")) +
                            replacement;
                    str0 = str0.substring(str0.indexOf("\r") + 2);
                }
                /*
                         while (str0.indexOf("\n") != -1) {
                 target += str0.substring(0, str0.indexOf("\n") - 1) + replacement;
                  str0 = str0.substring(str0.indexOf("\n") + 1);
                         }
                 */
            } else {
                while (str0.indexOf(finder) != -1) {
                    target += str0.substring(0, str0.indexOf(finder)) +
                            replacement;
                    //replacement = replacement.trim();
                    str0 = str0.substring(str0.indexOf(finder) + finder.length());
                }
            }
            target += str0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (target.equals("")) {
            target = str0;
        }
        return target;
    }

    // Splitting
    //-----------------------------------------------------------------------
    /**
     * <p>Splits the provided text into an array, using whitespace as the
     * separator.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.</p>
     *
     * <pre>
     * StringUtil.split(null)       = null
     * StringUtil.split("")         = []
     * StringUtil.split("abc def")  = ["abc", "def"]
     * StringUtil.split("abc  def") = ["abc", "def"]
     * StringUtil.split(" abc ")    = ["abc"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str) {
        return split(str, null, -1);
    }

    /**
     * <p>Splits the provided text into an array, separator specified.
     * This is an alternative to using StringTokenizer.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.</p>
     *
     * <pre>
     * StringUtil.split(null, “*”)         = null
     * StringUtil.split("", “*”)           = []
     * StringUtil.split("a.b.c", '.')    = ["a", "b", "c"]
     * StringUtil.split("a..b.c", '.')   = ["a", "b", "c"]
     * StringUtil.split("a:b:c", '.')    = ["a:b:c"]
     * StringUtil.split("a\tb\nc", null) = ["a", "b", "c"]
     * StringUtil.split("a b c", ' ')    = ["a", "b", "c"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChar  the character used as the delimiter,
     *  <code>null</code> splits on whitespace
     * @return an array of parsed Strings, <code>null</code> if null String input
     * @since 2.0
     */
    public static String[] split(String str, char separatorChar) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList<String>();
        int i = 0, start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * <p>Splits the provided text into an array, separators specified.
     * This is an alternative to using StringTokenizer.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> separatorChars splits on whitespace.</p>
     *
     * <pre>
     * StringUtil.split(null, “*”)         = null
     * StringUtil.split("", “*”)           = []
     * StringUtil.split("abc def", null) = ["abc", "def"]
     * StringUtil.split("abc def", " ")  = ["abc", "def"]
     * StringUtil.split("abc  def", " ") = ["abc", "def"]
     * StringUtil.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChars  the characters used as the delimiters,
     *  <code>null</code> splits on whitespace
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str, String separatorChars) {
        return split(str, separatorChars, -1);
    }

    /**
     * <p>Splits the provided text into an array, separators specified.
     * This is an alternative to using StringTokenizer.</p>
     *
     * <p>The separator is not included in the returned String array.
     * Adjacent separators are treated as one separator.</p>
     *
     * <p>A <code>null</code> input String returns <code>null</code>.
     * A <code>null</code> separatorChars splits on whitespace.</p>
     *
     * <pre>
     * StringUtil.split(null, “*”, “*”)            = null
     * StringUtil.split("", “*”, “*”)              = []
     * StringUtil.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
     * StringUtil.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
     * StringUtil.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StringUtil.split("ab:cd:ef", ":", 2)    = ["ab", "cdef"]
     * </pre>
     *
     * @param str  the String to parse, may be null
     * @param separatorChars  the characters used as the delimiters,
     *  <code>null</code> splits on whitespace
     * @param max  the maximum number of elements to include in the
     *  array. A zero or negative value implies no limit
     * @return an array of parsed Strings, <code>null</code> if null String input
     */
    public static String[] split(String str, String separatorChars, int max) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList<String>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        if (separatorChars == null) {
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = len;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                match = true;
                i++;
            }
        } else {
            StringTokenizer st = new 
                                                StringTokenizer(str,
                    separatorChars);
            while (st.hasMoreElements()) {
                String strTemp = (String) st.nextElement();
                list.add(strTemp);
            }


        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
    
    public static boolean isNotNull(String temp){
    	
    	if(temp!=null&&!("".equals(temp)))
    		return true;
    	
    	else return false;
    	
    }
    
    
    public static boolean isNull(String temp){
    	
    	if(temp==null||"".equals(temp))

    		return true;
    	else return false;
    }
    
    public static String replaceString(String strData, String regex,

            String replacement)

    {

        if (strData == null)

        {

            return null;

        }

        int index;

        index = strData.indexOf(regex);

        String strNew = "";

        if (index >= 0)

        {

            while (index >= 0)

            {

                strNew += strData.substring(0, index) + replacement;

                strData = strData.substring(index + regex.length());

                index = strData.indexOf(regex);

            }

            strNew += strData;

            return strNew;

        }

        return strData;

    }

    /*
     * 在字符串指定位置插入字符串
     */
    public static String getInsert(String str, String substr, int location) {
        String s1 = str;
        String s2 = substr;
        int i = location;
        String insertedStr = s1.substring(0, i) + s2
                + s1.substring(i, s1.length());

        return insertedStr;

    }
    
    public static String codepressCode(String code_body) {
    	code_body = StringUtil.replaceString(code_body, "\\\"","\"");
		code_body = StringUtil.replaceString(code_body, "\\\'","\'");
		code_body = StringUtil.replaceString(code_body, "\\n","\n");
		code_body = StringUtil.replaceString(code_body, "</script >","</script>");
		code_body = StringUtil.replaceString(code_body, "</SCRIPT >","</SCRIPT>");
		code_body = StringUtil.replaceString(code_body, "@/>", "><#list aaa as a >${a.PIC_NAME}</#list></@image>");
		code_body = StringUtil.replaceString(code_body, "* /","*/");
		return code_body;
    }
    
    public static String Null2Str(String object){
    	if(object==null)
    		return "";
    	else
    		return object;
    }


    
    public static String filterSstrToNull(String content){
    
    	for(String s:strFilterArr){
    		
    		content=StringUtil.replaceString(content, s, " ");
    	}
    	return content;
    }


    /**
	 * 将输入字符串按照空格分为子串，返回字串数组
	 * @param src	字符串源
	 * @return		分割后的字符串数组
	 */
	public static String[] splitToArray(String src)
	{
		if(src == null || "".equals(src))
			return null;
		
		src = src.trim();
		String[] words = src.split("\\s+");
		return words;
	}
	
	/**
	 * 判空。<br> 
	 * "" = true
	 * null = true
	 * "    " = true
	 * @param arg
	 * @return
	 */
	public static boolean isTrimEmpty(String arg){
		return StringUtils.isEmpty(arg) || StringUtils.isEmpty(arg.trim());
	}

	/**根据下划线（"_"）分隔字符串为长度为2的String数组 [0]:为ID [1]为CODE
	 * @param @param String
	 * @return String[]
	 */
	public static String[] splitForUnderline(String str){
		String[] arr = null;
		if(isNotNull(str)){
			arr = str.split("_");
			return arr;
		} else {
			return arr;
		}
	}
	
	public static boolean isNotNullForList(List list) {
		if (null != list && list.size() > 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isArrayNotNull(Object[] list) {
		if (null != list && list.length > 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isListNotNull(List list) {
		if (null != list && list.size() > 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isListNull(List list) {
		if (null == list || list.size() == 0) {
			return true;
		}
		return false;
	}
	
	public static String fileNameSpliceCsv(String fun, String userName) {
		String fileName = "";
		if (isNotEmpty(fun)) {
			fileName += fun + "_" ;
		}
		if (isNotEmpty(userName)) {
			fileName += userName + "_" ;
		}
		fileName += TimeUtil.format3Date(new Date()) + ".csv";
		return fileName;
	}
	
	public static boolean isTaoBaoChannel(String channelCode){
		if(ConstantValues.TB_CHANNEL_CODE.equals(channelCode)
				||ConstantValues.TMALL_CHANNEL_CODE.equals(channelCode)
				||ConstantValues.TBFX_CHANNEL_CODE.equals(channelCode)){
			return true;
		}
		return false;
	}
	
	public static String formatMoney(Object number){
		String pattern = "￥#,##0.00元";
		DecimalFormat nbFormat = new DecimalFormat();
		nbFormat.applyPattern(pattern);
		Object toFormatNumber = null;
		if(number == null){
			toFormatNumber = 0.00;
		}else if(String.class.getName().equalsIgnoreCase(number.getClass().getName())){
			try{
				toFormatNumber = Double.parseDouble(number.toString());
			}catch(Exception e){
				toFormatNumber = 0.00;
			}
		}else{
			toFormatNumber = number;
		}
		String result = nbFormat.format(toFormatNumber);
		return result;
	}
	
	
	/**
	 *去除特殊字符
	 */
	public static String filterEmoji(String source) {  
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find()) 
            {
                source = emojiMatcher.replaceAll("*");
                return source ; 
            }
        return source;
       }
       return source;  
    }
	
	public static String Null2TrimStr(String object){
    	if(object==null)
    		return "";
    	else
    		return object.trim();
    }
	
	private static int code = 0 ;
	
	public synchronized static String getSysCode(){
		String str = "";
			code +=1;
			if(code>0 && code<10){
				str="00"+code;
			}
			if(code>=10&&code<100){
				str="0"+code;
			}
			if(code>=100&&code<1000){
				str=""+code;
			}
			if(code>=1000){
				code=0;
				str="001";
			}
		if (isNotNull(str)) {str = System.currentTimeMillis() + str;}
		return str;
	}
}
