package com.github.blackbladeshiraishi.fm.moe.business.impl.moefm

import com.github.blackbladeshiraishi.fm.moe.business.business.ListHotRadios
import com.github.blackbladeshiraishi.fm.moe.domain.entity.Radio
import groovy.json.JsonSlurper
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class MoeFmListHotRadios implements ListHotRadios {

    String apiKey

    Retrofit retrofit

    @Override
    List<Radio> execute() {
        MoeFmService service = retrofit.create(MoeFmService);

        def response = service.listHotRadios(apiKey).execute()
        return parseText(response.body().string())
    }

    // ↓ for access of JsonSlurper().parseText()
    @TypeChecked(TypeCheckingMode.SKIP)
    private List<Radio> parseText(String body) {
        List<Radio> result = []
        def obj = new JsonSlurper().parseText(body)
        obj.response.hot_radios.each { rawRadio ->
            def radio = new Radio()
            radio.id = rawRadio.wiki_id
            radio.title = rawRadio.wiki_title
            result << radio
        }
        return result
    }

    public static interface MoeFmService {
        //http://moe.fm/explore?api=json&hot_radios=1&api_key={api_key}
        @GET('explore?api=json&hot_radios=1')
        Call<ResponseBody> listHotRadios(@Query('api_key') String apiKey);
    }

}
