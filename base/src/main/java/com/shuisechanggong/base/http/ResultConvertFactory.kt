package com.shuisechanggong.base.http

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.internal.bind.JsonTreeReader
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 *
 */

class ResultConvertFactory(val gson: Gson) : Converter.Factory() {

    companion object {
        fun create(): ResultConvertFactory =
            create(Gson())
        fun create(gson: Gson): ResultConvertFactory =
            ResultConvertFactory(gson)

    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {

        return ResultBodyConverter(gson, TypeToken.get(type))
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return ResultRequestBodyConverter(gson, adapter)
    }
}

internal class ResultBodyConverter<T>(val gson: Gson, val typeToken: TypeToken<T>) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {
        var jsonReader = gson.newJsonReader(value.charStream())
        try {

            if (ServerResult::class.java.equals(typeToken.rawType)) {
                var adapter = gson.getAdapter(typeToken)
                val result = adapter.read(jsonReader)
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw JsonIOException("JSON document was not fully consumed")
                }
                return result
            }
            var typeAdapter = gson.getAdapter(ServerResult::class.java)
            var serverResult = typeAdapter.read(jsonReader)

            if (serverResult.code != 1) {
                throw IOException("${serverResult.msg} code:${serverResult.code}")
            }

            if(Void::class.java.equals(typeToken.rawType) ||
                    Unit::class.java.equals(typeToken.rawType)){
                return null
            }

            var adapter = gson.getAdapter(typeToken)
            var reader = JsonTreeReader(serverResult.data)
            var result = adapter.read(reader)
            if (reader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed")
            }
            reader.close()

            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed")
            }
            return result
        } finally {
            value.close()
        }
    }

}

internal class ResultRequestBodyConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {
    private val MEDIA_TYPE: MediaType = MediaType.get("application/json; charset=UTF-8")
    private val UTF_8 = Charset.forName("UTF-8")

    override fun convert(value: T): RequestBody? {

        var buffer = Buffer()
        var writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        var jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }


}