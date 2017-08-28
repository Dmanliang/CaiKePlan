package com.example.caikeplan.logic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.caikeplan.R;
import com.example.caikeplan.activity.BannerLinkActivity;


/**
 * Created by DELL on 2017/4/12.
 */

public class AdFragment extends Fragment implements View.OnClickListener{

    private ImageView   mImageView;
    private String      imageUrl;
    private String      link;
    private String      title;

    public static AdFragment newInstance(String image_url, String link,String title) {
        AdFragment adFragment = new AdFragment();
        Bundle     bundle     = new Bundle();
        bundle.putString("image_url", image_url);
        bundle.putString("link", link);
        bundle.putString("title", title);
        adFragment.setArguments(bundle);
        return adFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        imageUrl = getArguments().getString("image_url");
        link = getArguments().getString("link");
        this.title = getArguments().getString("title");
        View view = inflater.inflate(R.layout.fragment_ad_layout, container, false);
        mImageView = (ImageView)view.findViewById(R.id.adv);
        Glide.with(getContext()).load(imageUrl).into(mImageView);
        mImageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.adv){
            Intent intent = new Intent(getContext(),BannerLinkActivity.class);
            intent.putExtra("url",link);
            intent.putExtra("title",title);
            startActivity(intent);
        }
    }
}
