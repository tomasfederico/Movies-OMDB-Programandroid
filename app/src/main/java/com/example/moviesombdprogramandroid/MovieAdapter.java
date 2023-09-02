package com.example.moviesombdprogramandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.MovieViewHolder> {
    /* El adapter se ejecuta a pedido del Sistema Operativo! Solo debemos programar su funcion interna */
    // SE BORRA EL LISTADO PARA EL LISTADAPTER >>> private List<Player> players;
    //private PlayerClickListener listener; // SOGA DEL CALLBACK PARA EL FRAGMENT!!!

    // COMUNICAR HACIA EL FRAGMENT CON LIVEDATA!
    private SingleLiveEvent<Movie> _movieSingleLiveEvent; // guion bajo para distinguir datos privados (antes era un mutablelivedata!!! refactorizado)
    private MutableLiveData<Integer> _itemCountLiveData; // avisa que se modifico la lista de jugadores

    protected MovieAdapter(List<Movie> movies) {
        // CONSTRUCTOR DEL ADAPTER QUE INCLUYE :
        // 2 LIVE DATA
        // METODO PARA AGREGAR LISTADO DE JUGADORES AL RECYCLERVIEW
        super(DIFF_CALLBACK);
        _movieSingleLiveEvent = new SingleLiveEvent<>();
        _itemCountLiveData = new MutableLiveData<>();
        setMoviesList(movies); // esto se usa para inicializar el listadapter
    }

    // METODO PARA AGREGAR LISTADO DE JUGADORES AL RECYCLERVIEW
    public void setMoviesList(List<Movie> movies){
        // EL submitList modifica el listado de jugadores del adapter.
        // EL RUNNABLE ES UN CALLBACK PARA ENVIAR INFORMACION EN CASO DE NECESIDAD.
        // EN ESTE CASO, SETEANDO UN LIVEDATA CON EL CONTEO DE ITEMS DE LA LISTA
        // PERMITE ACTUAR EN CONSECUENCIA EN EL FRAGMENT QUE CONTIENE AL ADAPTER.
        submitList(movies, new Runnable() {
            @Override
            public void run() {
                _itemCountLiveData.setValue(getItemCount());
            }
        });
    }


    // private MutableLiveData<Player> _playerMutableLiveData;... podria tambien ser mutable xq esta "arriba" en la jerarquia de la herencia.

    public LiveData<Movie> getMovieLiveData() { // Observar que lo devuelvo como livedata
        return _movieSingleLiveEvent;
    }
    public LiveData<Integer> getItemCountLiveData() {
        return _itemCountLiveData;
    }

/* TAMPOCO NECESITO DEL CONSTRUCTOR!
  //public PlayerAdapter(List<Player> playerList, PlayerClickListener listener) { ***esto era para callback***
    public PlayerAdapter(List<Player> playerList) {
        setPlayers(playerList);
        _playerSingleLiveEvent = new SingleLiveEvent<>(); // instancio la caja, lo que antes era un mutablelivedata ahora es un singlelivevent.
        //_playerMutableLiveData = new MutableLiveData<>(); // instancio la caja. (esto fue refactorizado)
    }
    // Metodo para modificar la lista de jugadores
    public void setPlayers(List<Player> playerList){
        this.players = playerList;
        return;
    }
*/
    // PARA APLICAR LO QUE NECESITA ESTE NUEVO LIST ADAPTER, LO TRAIGO DEL MANUAL DE ANDROID ONLINE
    // TENGO QUE ENSEÑARLE AL ADAPTER COMO COMPARAR 2 ITEMS.
    public static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Movie oldMovie, @NonNull Movie newMovie) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldMovie.getId() == newMovie.getId();
                }
                @Override
                public boolean areContentsTheSame(
                        // EN CASO QUE LOS ITEM SEAN IGUALES (=ID), PASA A ESTE METODO!
                        // ESTA COMPARANDO SI UN MISMO ITEM SE ACTUALIZO EN LA NUEVA LISTA DE ITEMS.
                        // POR EJEMPLO, SE ACTUALIZA UN PRECIO O DESCRIPCION, ETC...
                        @NonNull Movie oldMovie, @NonNull Movie newMovie) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldMovie.equals(newMovie);
                }
            };


    /* Es el CONSTRUCTOR del viewholder, infla layout de la celda que representara cada item, e instancia viewholder */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.player_item_layout, parent, false);

        // view.setOnClickListener(); // opcion A: Esto sirve para poder escuchar un clic del usuario en la celda.
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        // opcion B: Esto sirve para poder escuchar un clic del usuario en la celda.
        //viewHolder.itemView.setOnClickListener(new OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        // EJEMPLO: TOAST DEL NOMBRE DEL JUGADOR
        //        Player playerClicked = players.get(viewHolder.getAdapterPosition());
        //Toast.makeText(viewHolder.itemView.getContext(),playerClicked.getName(),Toast.LENGTH_LONG).show();
        //    }
        //});

        // Opcion C: USAR CALLBACK dentro del Listener y que ejecute el fragment la accion!
        viewHolder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    // esto ya no me sirve >> Player playerClicked = players.get(viewHolder.getAdapterPosition());
                    // nuevo listadapter necesita getitem para recuperar un listado de jugadores!
                    Movie movieClicked = getItem((viewHolder.getAdapterPosition()));

                    // CON CALLBACK
                    //if (listener != null) {
                        //listener.onPlayerClicked(playerClicked);
                    //}

                    // YA NO ME SIRVE EL LISTENER (CALLBACK), AHORA PONGO DIRECTAMENTE EL VALOR EN EL MUTABLELIVEDATA
                    _movieSingleLiveEvent.setValue(movieClicked);
                }
            }
        });


        return viewHolder;
    }

    /* Es el metodo que linkea la visual de cada celda con los datos de cada item de la lista */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        holder.onBind(movie);
    }


/*  EL GETITEMCOUNT YA NO SE USA EN EL LISTADAPTER
*   *//* Cantidad de elementos de la lista que se representara en el RV *//*
    @Override
    public int getItemCount() {
        if (players == null) {
            return 0;
        } else {
            return players.size();
        }
    }*/

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView movieTitle;
        private ImageView movieImage;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            // el viewholder ayuda al adapter asignando un objeto en java a cada item del layout
            movieTitle = itemView.findViewById(R.id.movie_title_id);
            movieImage = itemView.findViewById(R.id.movie_image_id);
        }

        // el viewholder ayuda con el "onBind" al Adapter a realizar la "pegatina" entre la visual y los datos.
        public void onBind(Movie movie) {
            movieTitle.setText(movie.getTitle());
            Glide.with(itemView.getContext()).load(movie.getImage()).circleCrop().into(movieImage);
        }
    }

    // CALLBACK para poder facilitarle un player al fragment.
//    public interface PlayerClickListener {
//        public void onPlayerClicked(Player player);
//    }

    // setListener definido para poder ejecutar el callback
//    public void setListener(PlayerClickListener listener) {
//        this.listener = listener;
//    }


}
