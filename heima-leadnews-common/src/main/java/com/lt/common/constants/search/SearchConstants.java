package com.lt.common.constants.search;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2023/2/4 16:55
 */
public interface SearchConstants {
    String ARTICLE_INDEX_NAME = "app_info_article";
    String ARTICLE_INDEX_MAPPING = "{\n" +
            "  \"mappings\":{\n" +
            "    \"properties\":{\n" +
            "        \"id\":{\n" +
            "          \"type\":\"long\"\n" +
            "        },\n" +
            "        \"publishTime\":{\n" +
            "          \"type\":\"date\"\n" +
            "        },\n" +
            "        \"layout\":{\n" +
            "          \"type\":\"integer\"\n" +
            "        },\n" +
            "        \"images\":{\n" +
            "          \"type\":\"keyword\",\n" +
            "          \"index\": false\n" +
            "        },\n" +
            "       \"staticUrl\":{\n" +
            "          \"type\":\"keyword\",\n" +
            "          \"index\": false\n" +
            "        },\n" +
            "        \"authorId\": {\n" +
            "      \t\t\"type\": \"long\"\n" +
            "   \t\t  },\n" +
            "        \"title\":{\n" +
            "          \"type\":\"text\",\n" +
            "          \"analyzer\":\"ik_smart\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
