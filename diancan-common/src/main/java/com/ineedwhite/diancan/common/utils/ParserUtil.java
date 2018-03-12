package com.ineedwhite.diancan.common.utils;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ruanxin
 * @create 2018-03-12
 * @desc
 */
public class ParserUtil {
    private static final Logger logger = Logger.getLogger(ParserUtil.class);

    private static final String REGX_PREFIX = "\\[\\{";

    private static final String REGX_SUFFIX = "\\}\\]";

    public static String JsonHandler(String jsonStr) {
        Pattern pattern_pre = Pattern.compile(REGX_PREFIX);
        Matcher matcher_pre = pattern_pre.matcher(jsonStr);
        String tmp = matcher_pre.replaceAll("{");

        Pattern pattern_suf = Pattern.compile(REGX_SUFFIX);
        Matcher matcher_suf = pattern_suf.matcher(tmp);
        String res = matcher_suf.replaceAll("}");
        return res;
    }

    public static void main(String[] args) {
    }
}
