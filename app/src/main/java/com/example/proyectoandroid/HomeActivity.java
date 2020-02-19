package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.model.Home_noticiasRecView;
import com.example.model.Libros;
import com.example.model.LibrosRecView;
import com.example.model.Noticia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView ultimasEntradas;
    private ArrayList<Libros> libros;
    private RecyclerView ultimasNoticias;
    private ArrayList<Noticia> noticias;
    private HashMap<String, HashMap<String, String>> listaAlmacen;
    private HashMap<String, HashMap<String, String>> listaAutores;
    private HashMap<String, HashMap<String, String>> listaLibros;
    private HashMap<String, HashMap<String, String>> listaNoticias;
    private HashMap<String, HashMap<String, HashMap<String, String>>> listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //datos de usuario guardados y autentificados
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Bundle extras = this.getIntent().getExtras();
        listado = (HashMap<String, HashMap<String, HashMap<String, String>>>) extras.getSerializable("listado");
        listaAlmacen = listado.get("listaAlmacen");
        listaAutores = listado.get("listaAutores");
        listaLibros = listado.get("listaLibros");
        listaNoticias = listado.get("listaNoticias");

        libros = new ArrayList<Libros>();

        for (Map.Entry<String, HashMap<String, String>> almacen : listaAlmacen.entrySet()) {
            Libros libro = new Libros();
            for (Map.Entry<String, String> item : listaAlmacen.get(almacen.getKey()).entrySet()) {
                if (item.getKey().equals("codlibro")) {

                    String titulo = listaLibros.get(item.getValue()).get("titulo");
                    String publicacion = listaLibros.get(item.getValue()).get("publicacion");
                    String idAutor = listaLibros.get(item.getValue()).get("codAutor");
                    String idLibro = listaLibros.get(item.getValue()).get("idLibro");
                    String autor = listaAutores.get(idAutor).get("nombre");
                    String volumen = listaAlmacen.get(almacen.getKey()).get("volumen");
                    String imagen = listaAlmacen.get(almacen.getKey()).get("imagen");
                    String sinopsis = listaLibros.get(item.getValue()).get("sinopsis");
                    libro.setCodLibro(Integer.parseInt(idLibro));
                    libro.setAutor(autor);
                    libro.setTitulo(titulo);
                    libro.setPublicacion(publicacion);
                    libro.setVolumen(volumen);
                    libro.setImagen(imagen);
                    libro.setSinopsis(sinopsis);
                }
            }
            libros.add(libro);
        }

        noticias = new ArrayList<Noticia>();

        for (HashMap<String, String> noticia : listaNoticias.values()) {
            noticias.add(new Noticia(Integer.parseInt(noticia.get("codnoticia")), noticia.get("imagen"), noticia.get("titulo"), noticia.get("descripcion")));
        }


        ultimasEntradas = (RecyclerView) findViewById(R.id.home_ultimas_entradas);
        ultimasEntradas.setHasFixedSize(true);
        final LibrosRecView listaEntrada = new LibrosRecView(libros);

        listaEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Libros libro = libros.get(ultimasEntradas.getChildAdapterPosition(v));
                Bundle b = new Bundle();
                b.putSerializable("libro", libro);
                Intent i = new Intent(HomeActivity.this, ItemActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

        ultimasEntradas.setAdapter(listaEntrada);
        ultimasEntradas.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        ultimasNoticias = (RecyclerView) findViewById(R.id.home_ultimas_noticias);
        ultimasNoticias.setHasFixedSize(true);
        final Home_noticiasRecView listaNoticias = new Home_noticiasRecView(noticias);

        listaNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Noticia noticia = noticias.get(ultimasNoticias.getChildAdapterPosition(v));

                Bundle b = new Bundle();
                b.putSerializable("noticia", noticia);
                Intent i = new Intent(HomeActivity.this, NewsActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });


        ultimasEntradas.setAdapter(listaEntrada);
        ultimasEntradas.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ultimasNoticias.setAdapter(listaNoticias);
        ultimasNoticias.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        Bundle extras = this.getIntent().getExtras();
        listado = (HashMap<String, HashMap<String, HashMap<String, String>>>) extras.getSerializable("listado");
        extras = new Bundle();
        extras.putSerializable("listado", listado);
        switch (item.getItemId()) {
            case R.id.inicio:
                return true;
            case R.id.lista:
                extras.putSerializable("libros",libros);
                i = new Intent(HomeActivity.this, ListaActivity.class);
                i.putExtras(extras);
                startActivity(i);
                return true;
            case R.id.carrito:
                Log.i("ActionBar", "Settings!");
                return true;
            case R.id.user:
                extras.putSerializable("libros",libros);
                i = new Intent(HomeActivity.this, UsuarioActivity.class);
                i.putExtras(extras);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
