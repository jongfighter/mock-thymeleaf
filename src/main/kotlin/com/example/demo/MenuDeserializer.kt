package com.example.demo

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.ObjectCodec
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.*


class MenuDeserializer : JsonDeserializer<Menu>() {

    private var objectMapper = ObjectMapper();

    override fun deserialize(p: JsonParser?, p1: DeserializationContext?): Menu? {
        var objectCodec: ObjectCodec = p!!.codec
        var jsonNode: JsonNode = objectCodec.readTree(p)
        var href = jsonNode["href"].asText()
        var text = jsonNode["text"].asText()
        val children: LinkedHashSet<Menu> = objectMapper.convertValue(jsonNode["menu"], object : TypeReference<LinkedHashSet<Menu>>() {})
        return Menu(href, text, children);
    }
}

