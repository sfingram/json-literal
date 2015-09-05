/**
 * Copyright 2009-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.jsonliteral.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import net.javacrumbs.jsonliteral.core.lambda.NamedValue;

import static java.util.Arrays.asList;

public class JsonLiteral {

    private static JsonNodeFactory nodeFactory = new ObjectMapper().getNodeFactory();

    @SafeVarargs
    public static JsonNode obj(NamedValue<Object>... keyValuePairs) {
        ObjectNode node = new ObjectNode(nodeFactory);
        asList(keyValuePairs)
                .stream()
                .forEach(kvp -> put(node, kvp));
        return node;
    }

    private static void put(ObjectNode node, NamedValue<Object> kvp) {
        node.put(kvp.name(), convertValueToNode(kvp.value()));
    }

    private static JsonNode convertValueToNode(Object value) {
        if (value instanceof Boolean) {
            return BooleanNode.valueOf((Boolean) value);
        } else if (value instanceof JsonNode) {
            return (JsonNode) value;
        } else if (value instanceof String) {
            return TextNode.valueOf((String) value);
        } else if (value instanceof Integer) {
            return IntNode.valueOf((Integer) value);
        } else if (value instanceof Long) {
            return LongNode.valueOf((Long) value);
        } else if (value instanceof Double) {
            return DoubleNode.valueOf((Double) value);
        } else if (value instanceof Float) {
            return FloatNode.valueOf((Float) value);
        } else if (value instanceof Iterable) {
            ArrayNode arrayNode = new ArrayNode(nodeFactory);
            for (Object a : (Iterable<?>) value) {
                arrayNode.add(convertValueToNode(a));
            }
            return arrayNode;
        } else if (value == null) {
           return NullNode.getInstance();
        } else {
            throw new IllegalArgumentException("Can not serialize type " + value.getClass());
        }
    }
}
