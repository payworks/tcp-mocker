package io.payworks.labs.tcpmocker.test;

import io.payworks.labs.tcpmocker.support.resource.ResourceUtils;

import java.util.LinkedHashSet;
import java.util.Set;

public final class TcpMappingsRegistry {

    public static final String TEST_MAPPINGS_PATH = "classpath:/tcp-mappings/";
    public static final Set<String> TEST_MAPPINGS = new LinkedHashSet<>();

    public static final String TEST_DEFAULT_JSON_MAPPING_1 = filePath("/test-default-json-mapping1.json");
    public static final String TEST_DEFAULT_YAML_MAPPING_1 = filePath("/test-default-yaml-mapping1.yml");
    public static final String TEST_DYNAMIC_JSON_MAPPING_1 = filePath("/test-dynamic-json-mapping1.json");
    public static final String TEST_DYNAMIC_JSON_MAPPING_2 = filePath("/test-dynamic-json-mapping2.json");
    public static final String TEST_LIST_YAML_MAPPING_1 = filePath("/test-list-yaml-mapping1.yml");
    public static final String TEST_LIST_JSON_MAPPING_1 = filePath("/test-list-json-mapping1.json");
    public static final String TEST_ORDERED_JSON_MAPPING_1 = filePath("/test-ordered-json-mapping1.json");
    public static final String TEST_ORDERED_JSON_MAPPING_2 = filePath("/test-ordered-json-mapping2.json");
    public static final String TEST_ORDERED_JSON_MAPPING_3 = filePath("/test-ordered-json-mapping3.json");
    public static final String TEST_MULTIREQUEST_JSON_MAPPING_1 = filePath("/test-multirequest-json-mapping1.json");
    public static final String TEST_MULTIREQUEST_YAML_MAPPING_1 = filePath("/test-multirequest-yaml-mapping1.yml");


    private TcpMappingsRegistry() {
    }

    private static String filePath(final String s) {
        final String filePath = ResourceUtils.concat(TEST_MAPPINGS_PATH, s);
        TEST_MAPPINGS.add(filePath);
        return filePath;
    }
}
