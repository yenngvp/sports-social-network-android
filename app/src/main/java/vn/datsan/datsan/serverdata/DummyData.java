package vn.datsan.datsan.serverdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.datsan.datsan.models.ImageModel;

/**
 * Created by xuanpham on 7/30/16.
 */
public class DummyData {
    public static Map<String, String> getImages() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
        url_maps.put("deadpool", "http://api.androidhive.info/images/glide/medium/deadpool.jpg");
        url_maps.put("bvs", "http://api.androidhive.info/images/glide/medium/bvs.jpg");
        url_maps.put("cacw", "http://api.androidhive.info/images/glide/medium/cacw.jpg");
        url_maps.put("squad", "http://api.androidhive.info/images/glide/medium/squad.jpg");
        url_maps.put("bourne", "http://api.androidhive.info/images/glide/medium/bourne.jpg");
        url_maps.put("doctor", "http://api.androidhive.info/images/glide/medium/doctor.jpg");
        url_maps.put("dory", "http://api.androidhive.info/images/glide/medium/dory.jpg");
        url_maps.put("hunger", "http://api.androidhive.info/images/glide/medium/hunger.jpg");
        url_maps.put("hours", "http://api.androidhive.info/images/glide/medium/hours.jpg");
        url_maps.put("ipman3", "http://api.androidhive.info/images/glide/medium/ipman3.jpg");
        url_maps.put("book", "http://api.androidhive.info/images/glide/medium/book.jpg");
        url_maps.put("xmen", "http://api.androidhive.info/images/glide/medium/xmen.jpg");

        url_maps.put("deadpool2", "http://api.androidhive.info/images/glide/medium/deadpool.jpg");
        url_maps.put("bvs2", "http://api.androidhive.info/images/glide/medium/bvs.jpg");
        url_maps.put("cacw2", "http://api.androidhive.info/images/glide/medium/cacw.jpg");
        url_maps.put("squad2", "http://api.androidhive.info/images/glide/medium/squad.jpg");
        url_maps.put("bourne2", "http://api.androidhive.info/images/glide/medium/bourne.jpg");
        url_maps.put("doctor2", "http://api.androidhive.info/images/glide/medium/doctor.jpg");
        url_maps.put("dory2", "http://api.androidhive.info/images/glide/medium/dory.jpg");
        url_maps.put("hunger2", "http://api.androidhive.info/images/glide/medium/hunger.jpg");
        url_maps.put("hours2", "http://api.androidhive.info/images/glide/medium/hours.jpg");
        url_maps.put("ipman32", "http://api.androidhive.info/images/glide/medium/ipman3.jpg");
        url_maps.put("book2", "http://api.androidhive.info/images/glide/medium/book.jpg");
        url_maps.put("xmen2", "http://api.androidhive.info/images/glide/medium/xmen.jpg");
        return url_maps;
    }

    public static List<ImageModel> getImageModels() {
        Map<String, String> images = getImages();
        List<ImageModel> imageModels = new ArrayList<>();
        for (String key : images.keySet()) {
            ImageModel imageModel = new ImageModel(images.get(key), key);
            imageModels.add(imageModel);
        }
        return imageModels;
    }
}
