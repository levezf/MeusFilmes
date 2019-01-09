package com.felipe.levez.listadefilmes.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.felipe.levez.listadefilmes.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ImageDetailsActivity extends AppCompatActivity {


    private static final String EXTRA_NOME_FILME = "nome_filme";
    private String linkImage;
    private static final String EXTRA_LINK_IMAGE = "link_image";
    private String nome_filme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            linkImage = Objects.requireNonNull(getIntent().getExtras()).getString(EXTRA_LINK_IMAGE);
            nome_filme = getIntent().getExtras().getString(EXTRA_NOME_FILME);
        }else{
            linkImage = savedInstanceState.getString(EXTRA_LINK_IMAGE);
            nome_filme = savedInstanceState.getString(EXTRA_NOME_FILME);
        }

        setContentView(R.layout.activity_image_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(nome_filme);

        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


        ImageView iv_imagemExpandida = findViewById(R.id.iv_imagem_details_expandida);

        Picasso.get().load(linkImage).into(iv_imagemExpandida);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_LINK_IMAGE, linkImage);
        outState.putString(EXTRA_NOME_FILME, nome_filme);
    }
}
