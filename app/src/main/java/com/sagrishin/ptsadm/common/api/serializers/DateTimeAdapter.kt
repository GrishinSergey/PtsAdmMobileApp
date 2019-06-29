package com.sagrishin.ptsadm.common.api.serializers

import com.google.gson.*
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.lang.reflect.Type

class DateTimeAdapter : JsonSerializer<DateTime>, JsonDeserializer<DateTime?> {

    override fun serialize(src: DateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(ISODateTimeFormat.dateTime().print(src))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DateTime? {
        return if ((json?.asString == null) || json.asString.isEmpty()) {
            null
        } else {
            ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(json.asString)
        }
    }

}
