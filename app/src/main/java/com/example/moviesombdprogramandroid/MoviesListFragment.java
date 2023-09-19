package com.example.moviesombdprogramandroid;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesombdprogramandroid.local.DatabaseHandler;
import com.example.moviesombdprogramandroid.local.MovieEntity;
import com.example.moviesombdprogramandroid.local.MovieLocalDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesListFragment extends Fragment {
    // DECLARACION DE ATRIBUTOS DEL FRAGMENT
    private RecyclerView playersRecyclerview;
    private MovieAdapter movieAdapter;
    private ImageView emptyStateImageView;
    private Group moviesReciclerviewGroup;
    private Group moviesEmptyStateGroup;
    private FloatingActionButton changeListFloatingButton;
    private FloatingActionButton removeListFloatingButton;

    // CREACION DEL MOVIES DAO QUIEN MANEJARA EL ACCESO A LA INFORMACION DE LA API.
    // esto deberia estar en la parte del repository del viewmodel.
    private MoviesDAO moviesDAO;
    // handler de DB. esto tambien deberia estar en el repository del mvvm
    private DatabaseHandler databaseHandler;
    private MovieLocalDAO movieLocalDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        movieAdapter = new MovieAdapter(null);
        moviesDAO = new MoviesDAO();

        // ***CALLBACK***
        // Ya tengo definido un Adapter como atributo del Fragment.
        // Le puedo pasar Soga del callback como parametro al constructor del adapter
        // Puedo implementar un playerClickListener como un "fantasma" xq el Fragment tiene un Adapter como atributo!
        // OBSERVAR: se instancia el playerClickListener fantasma desde el PlayerAdapter >>> PlayerAdapter.____
//        playerAdapter = new PlayerAdapter(players, new PlayerAdapter.PlayerClickListener() {
//            @Override
//            public void onPlayerClicked(Player player) {
//                Toast.makeText(getContext(),player.getName(),Toast.LENGTH_SHORT).show();
//            }
//        });

        // instanciar objeto DAO para comunicarse con api.
        moviesDAO = new MoviesDAO();
        // instanciar handler y objeto DAO para comunicarse con DB
        databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
        movieLocalDAO = databaseHandler.getMovieLocalDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        playersRecyclerview = view.findViewById(R.id.players_recycler_view_id);
        emptyStateImageView = view.findViewById(R.id.players_empty_state_id); // para cuando no hay items en lista, empty state
        // Group link de xml a java
        moviesReciclerviewGroup = view.findViewById(R.id.players_reciclerview_group);
        moviesEmptyStateGroup = view.findViewById(R.id.empty_state_group);
        // INSTANCIAR FLOATING BUTTON PARA CAMBIAR DE LISTA DE JUGADORES
        changeListFloatingButton = view.findViewById(R.id.change_list_floating_button);
        removeListFloatingButton = view.findViewById(R.id.remove_list_floating_button);
        /**El recycler para poder funcionar necesita de dos cosas:
         * 1) Adapter
         * 2) LayoutManager
         *
         *
         * El LayoutManager es cómo será la disposición estructural de las celdas en el recycler. Las opciones más comunes son:
         * a) LinearLayoutManager ---> De forma lineal:
         *    a1) Orientación Vertical
         *    a2) Orientación Horizontal
         * b) GridLayoutManager ----> De forma de Grilla (Cuadricula):
         *    b1) Oritentación Vertical
         *    b2) Orientación Horizontal*/
        /**
         * Para crear un LinearLayoutManager necesitamos 3 parámetros:
         * 1) Context: por eso llamamos a getActivity()
         * 2) La orientación: Vertical / Horizontal
         * 3) ReverseLayout (El sentido/dirección inicial del scroll): Si ponemos "false" entonces el usuario verá el elemento inicial del listado, arriba de T0Do en la pantalla
         * y deberá scrollear hacia abajo para ver el resto de los elementos. Ese uso es el más común y lo vemos en los contactos de whatsapp, los mails, feed IG, etc.
         * Si en cambio ponemos "true" veremos al primer elemento, abajo de T0do en la pantalla y deberá scrollear hacia arriba para ver el resto de los elementos.
         * Este comportamiento lo podemos observar por lo general en los chats, donde le mensaje más reciente en la conversación es el de más abajo.*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        /**Muestro simplemente cómo crear un GridLayoutManaager en donde la principal diferencia es que debemos indicar en el segundo parametro
         * la cantidad de columnas, llamado "spanCount" de la grilla. En nuestro caso por ejemplo ponemos 4 como spanCount. */
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        /**Así se le setea al recycler view cuál será el layout manager que usará. */
        playersRecyclerview.setLayoutManager(linearLayoutManager);
        /**Así se le setea al recycler view cuál será el adapter que usará. */
        playersRecyclerview.setAdapter(movieAdapter);

        /**Comentario importante: el seteo del layout manager puede hacerse también directamente en el layout.xml donde se definicio al <RecyclerView>
         * Allí debemos completar las propiedades: app:layoutManager android:orientation app:reverseLayout y para el caso de grilla app:spanCount*/

        getMovies("jurassic");
        return view;
    }

    private void getMovies(String movieTitle) {
        // aca se produce la comunicacion!
        // voy a buscar con el DAO los endpoints y de ahi elijo cual usar. en este caso, get movies x nombre
        // esto me lo devuelve en un CALL, es decir, una "respuesta" de la query que le solicitamos a la api.
        // en general, se solicita mediante un enqueue xq retrofit x detras maneja los threads
        // al enqueue logicamente se le debe pasar un callback.
        moviesDAO.getMoviesEndpoints().getMoviesByName("78e7f4b6", movieTitle).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response != null) {
                    MoviesResponse moviesResponse = response.body();
                    List<Movie> movies = moviesResponse.getMovies(); // devuelve listado de peliculas de la api, almaceno en una variable
                    movieAdapter.setMoviesList(movies);
                    List<MovieEntity> movieEntities = new ArrayList<>();
                    movieEntities = transformMoviesToEntities(movies);
                    try {
                        movieLocalDAO.insertAllMovies(movieEntities);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "comunicacion OK", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(getContext(), "comunicacion fallo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<MovieEntity> getMovieEntitiesFromDataBase(){
        List<MovieEntity> movieEntities = databaseHandler.getMovieLocalDao().getNinetiesMovies();
        return movieEntities;
    }

    private List <MovieEntity> transformMoviesToEntities(List<Movie> movies){
        List<MovieEntity> movieEntities = new ArrayList<>();
        for (Movie movie : movies) {
            String id = movie.getId();
            String title = movie.getTitle();
            String image = movie.getImage();
            String year = movie.getYear();
            MovieEntity movieEntity = new MovieEntity(id, title, image, year);
            movieEntities.add(movieEntity);
        }
        return movieEntities;
    }

    private List <Movie> transformEntitiesToMovies(List<MovieEntity> movieEntities){
        List<Movie> movies = new ArrayList<>();
        for (MovieEntity movieEntity : movieEntities) {
            String id = movieEntity.getId();
            String title = movieEntity.getTitle();
            String image = movieEntity.getImage();
            String year = movieEntity.getYear();
            Movie movie = new Movie(id, title, image, year);
            movies.add(movie);
        }
        return movies;
    }

    // manejar la logica del empty state
    // con la posibilidad de tener diferentes tipos de listas
    // debo generar otra logica para el empty state.
    // Esta se debe encontrar dentro del manejo del fragment
    private void emptyStateHandler(Integer itemCount) {
        // CASO EMPTY STATE, ES DECIR,
        // NO TENGO NADA QUE MOSTRAR EN EL RECICLER VIEW XQ LA LISTA ESTA VACIA,
        // *** ATENCION! >>> NO ES NULA ***
        boolean hasEmptyItems = (itemCount == 0); // Compara integer con 0 >> se traduce a boolean false o true.
        if (hasEmptyItems) {
            // MUESTRO EMPTY STATE ahora mejorado con groups.
            moviesEmptyStateGroup.setVisibility(View.VISIBLE);
            moviesReciclerviewGroup.setVisibility(View.INVISIBLE);
        } else {
            // CASO CONTRARIO, ES DECIR, TENGO ITEMS QUE MOSTRAR EN EL RECICLER VIEW, INVIERTO VISIBILIDAD.
            moviesEmptyStateGroup.setVisibility(View.INVISIBLE);
            moviesReciclerviewGroup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        removeListFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Anulo la lista del adapter, que es la que se esta mostrando en pantalla
                List<MovieEntity> movieEntities = getMovieEntitiesFromDataBase();
                List<Movie> movies = transformEntitiesToMovies(movieEntities);
                movieAdapter.setMoviesList(movies);

            }
        });

        changeListFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovies("Boca Juniors");// cambio de listado de peliclas por una que se llame BOCA
            }
        });

        changeListFloatingButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "CAMBIAR DE LISTADO", Toast.LENGTH_SHORT).show();
                return true; // si lo seteas en true >>> no ejecuta el onclick "corto". False > ejecuta accion de pulsado corto.
            }
        });

        movieAdapter.getMovieLiveData().observe(getViewLifecycleOwner(), new Observer<Movie>() {
            // Parametro 1 > le paso el ciclo de vida del fragment
            // Parametro 2 > creo un nuevo observer "fantasma" > fijarse que es un callback en realidad.
            @Override
            public void onChanged(Movie movie) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MovieDetailFragment.MOVIE_KEY, movie);
                NavHostFragment.findNavController(MoviesListFragment.this).navigate(R.id.action_moviesListFragment_to_moviesDetailFragment, bundle);
            }
        });
        movieAdapter.getItemCountLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer itemCount) {
                emptyStateHandler(itemCount);
            }
        });
    }

}
