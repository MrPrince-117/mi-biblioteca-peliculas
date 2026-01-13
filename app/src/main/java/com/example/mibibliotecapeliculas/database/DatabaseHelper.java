package com.example.mibibliotecapeliculas.database;


//Uso SQLite con SQLiteOpenHelper.
//He creado varias tablas relacionadas y cada película se guarda asociada a un usuario,
// de forma que cada uno solo ve sus propios datos

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mibibliotecapeliculas.models.Pelicula;

import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper  extends SQLiteOpenHelper {


    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;

    // ===== TABLAS =====
    private static final String TABLE_USUARIOS = "usuarios";
    private static final String TABLE_GENEROS = "generos";
    private static final String TABLE_PELICULAS = "peliculas";

    // ===== COLUMNAS COMUNES =====
    private static final String COLUMN_ID = "id";

    // ===== USUARIOS =====
    private static final String COLUMN_USUARIO = "nombre_usuario";
    private static final String COLUMN_PASSWORD = "contrasena";

    // ===== GENEROS =====
    private static final String COLUMN_NOMBRE_GENERO = "nombre";

    // ===== PELICULAS =====
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_ANIO = "anio";
    private static final String COLUMN_ID_GENERO = "id_genero";
    private static final String COLUMN_VALORACION = "valoracion";
    private static final String COLUMN_DESCRIPCION = "descripcion";
    private static final String COLUMN_PORTADA = "portada";
    private static final String COLUMN_ID_USUARIO = "id_usuario";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // =====================================================
    // CREACIÓN DE TABLAS
    // =====================================================
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tabla usuarios
        String CREATE_USUARIOS = "CREATE TABLE " + TABLE_USUARIOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USUARIO + " TEXT UNIQUE NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(CREATE_USUARIOS);

        // Tabla géneros
        String CREATE_GENEROS = "CREATE TABLE " + TABLE_GENEROS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOMBRE_GENERO + " TEXT NOT NULL)";
        db.execSQL(CREATE_GENEROS);

        // Tabla películas
        String CREATE_PELICULAS = "CREATE TABLE " + TABLE_PELICULAS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITULO + " TEXT NOT NULL, "
                + COLUMN_ANIO + " INTEGER, "
                + COLUMN_ID_GENERO + " INTEGER, "
                + COLUMN_VALORACION + " REAL, "
                + COLUMN_DESCRIPCION + " TEXT, "
                + COLUMN_PORTADA + " INTEGER, "
                + COLUMN_ID_USUARIO + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_ID_GENERO + ") REFERENCES " + TABLE_GENEROS + "(" + COLUMN_ID + "), "
                + "FOREIGN KEY(" + COLUMN_ID_USUARIO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_PELICULAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PELICULAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENEROS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }

    // =====================================================
    // USUARIOS
    // =====================================================

    // INSERT usuario
    public void addUsuario(String usuario, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO, usuario);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USUARIOS, null, values);
        db.close();
    }

    // LOGIN usuario
    public int loginUsuario(String usuario, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USUARIOS,
                new String[]{COLUMN_ID},
                COLUMN_USUARIO + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{usuario, password},
                null, null, null);

        int idUsuario = -1;
        if (c.moveToFirst()) {
            idUsuario = c.getInt(0);
        }

        c.close();
        db.close();
        return idUsuario;
    }

    // =====================================================
    // GÉNEROS
    // =====================================================

    public void addGenero(String nombre) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE_GENERO, nombre);
        db.insert(TABLE_GENEROS, null, values);
        db.close();
    }

    public List<String> getAllGeneros() {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT " + COLUMN_NOMBRE_GENERO + " FROM " + TABLE_GENEROS, null);

        if (c.moveToFirst()) {
            do {
                lista.add(c.getString(0));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return lista;
    }

    // =====================================================
    // PELÍCULAS (CRUD)
    // =====================================================

    // INSERT
    public void addPelicula(String titulo, int anio, int idGenero,
                            float valoracion, String descripcion,
                            int portada, int idUsuario) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITULO, titulo);
        values.put(COLUMN_ANIO, anio);
        values.put(COLUMN_ID_GENERO, idGenero);
        values.put(COLUMN_VALORACION, valoracion);
        values.put(COLUMN_DESCRIPCION, descripcion);
        values.put(COLUMN_PORTADA, portada);
        values.put(COLUMN_ID_USUARIO, idUsuario);

        db.insert(TABLE_PELICULAS, null, values);
        db.close();
    }

    // SELECT
    public List<Pelicula> getPeliculasByUsuario(int idUsuario) {

        List<Pelicula> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT p.id, p.titulo, p.anio, g.nombre, p.valoracion, p.portada " +
                        "FROM " + TABLE_PELICULAS + " p " +
                        "LEFT JOIN " + TABLE_GENEROS + " g ON p.id_genero = g.id " +
                        "WHERE p.id_usuario = ?",
                new String[]{String.valueOf(idUsuario)}
        );

        if (c.moveToFirst()) {
            do {
                Pelicula p = new Pelicula(
                        c.getInt(0),
                        c.getString(1),
                        c.getInt(2),
                        c.getString(3),
                        c.getFloat(4),
                        c.getInt(5)
                );
                lista.add(p);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return lista;
    }

    // DELETE
    public void deletePelicula(int idPelicula) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PELICULAS, COLUMN_ID + "=?", new String[]{String.valueOf(idPelicula)});
        db.close();
    }

    // UPDATE
    public void updatePelicula(int idPelicula, String titulo, int anio,
                               int idGenero, float valoracion,
                               String descripcion, int portada) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITULO, titulo);
        values.put(COLUMN_ANIO, anio);
        values.put(COLUMN_ID_GENERO, idGenero);
        values.put(COLUMN_VALORACION, valoracion);
        values.put(COLUMN_DESCRIPCION, descripcion);
        values.put(COLUMN_PORTADA, portada);

        db.update(TABLE_PELICULAS, values,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(idPelicula)});
        db.close();
    }

    // SELECT: Película por ID
    public Pelicula getPeliculaById(int idPelicula) {

        SQLiteDatabase db = getReadableDatabase();
        Pelicula pelicula = null;

        Cursor c = db.rawQuery(
                "SELECT p.id, p.titulo, p.anio, g.nombre, p.valoracion, p.descripcion, p.portada " +
                        "FROM peliculas p " +
                        "LEFT JOIN generos g ON p.id_genero = g.id " +
                        "WHERE p.id = ?",
                new String[]{String.valueOf(idPelicula)}
        );

        if (c.moveToFirst()) {
            pelicula = new Pelicula(
                    c.getInt(0),
                    c.getString(1),
                    c.getInt(2),
                    c.getString(3),
                    c.getFloat(4),
                    c.getInt(6)
            );
            pelicula.setDescripcion(c.getString(5));
        }

        c.close();
        db.close();
        return pelicula;
    }

}
