package com.sagrishin.ptsadm.common.api.serializers

import com.google.gson.*
import org.joda.time.LocalDateTime
import org.joda.time.format.ISODateTimeFormat
import java.lang.reflect.Type

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime?> {

    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(ISODateTimeFormat.dateTime().print(src))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime? {
        return if ((json?.asString == null) || json.asString.isEmpty()) {
            null
        } else {
            ISODateTimeFormat.localDateOptionalTimeParser().withOffsetParsed().parseLocalDateTime(json.asString)
        }
    }

}
