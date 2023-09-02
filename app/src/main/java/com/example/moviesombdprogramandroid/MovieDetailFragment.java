package com.example.moviesombdprogramandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class MovieDetailFragment extends Fragment {

    // constante para el bundle que viene de fragment anterior
    public static final String MOVIE_KEY = "MOVIE_KEY";
    // DEFINIR ATRIBUTOS DEL FRAGMENT
    private TextView movieTitle;
    private TextView movieDescription;
    private ImageView movieImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_detail, container, false);

        movieTitle = view.findViewById(R.id.movie_title_id);
        movieDescription = view.findViewById(R.id.player_description_id);
        movieImage = view.findViewById(R.id.movie_image_id);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Me traigo lo que llega del bundle
        Bundle bundle = getArguments();
        Movie movie = (Movie) bundle.getSerializable(MOVIE_KEY);
        // Asigno la info del objeto a las entidades de la visual que deseo mostrar.
        movieTitle.setText(movie.getTitle());
        movieDescription.setText(movie.getYear());
        // Glide desde un fragment puede apalancarse en el Activity que lo contiene!
        Glide.with(getActivity()).load(movie.getImage()).circleCrop().into(movieImage);
    }
}