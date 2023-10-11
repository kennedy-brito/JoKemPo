package com.example.jokempo.pessoaDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jokempo.pessoa.Pessoa;

import java.util.ArrayList;

public class PessoaDao extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NOME_BANCO = "DBJogador.db";
    private static final String TABELA = "Jogador";
    private static final String ID = "Id";
    private static final String NOME = "Nome";
    private static final String PARTIDAS = "Partidas";
    private static final String VITORIAS = "Vitorias";
    private static final String TEMPO_JOGO = "tempoJogo";


    public PessoaDao(Context context){super(context, NOME_BANCO, null, VERSION);}
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABELA + "("
                + ID + " integer primary key autoincrement, "
                + NOME + " TEXT, "
                + PARTIDAS + " integer, "
                + VITORIAS + " integer, "
                + TEMPO_JOGO + " integer"
                + ");";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE " + TABELA;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    public long salvarPessoa(Pessoa p){

        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(NOME, p.getNome());
        values.put(VITORIAS, p.getQtdVitorias());
        values.put(PARTIDAS, p.getQtdPartidas());
        values.put(TEMPO_JOGO, p.getHorasJogadas());

        retornoDB = getWritableDatabase().insert(TABELA, null, values);

        return retornoDB;
    }

    public long alterarPessoa(Pessoa p){
        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(NOME, p.getNome());

        String[] args = {String.valueOf(p.getId())};
        retornoDB = getWritableDatabase().update(TABELA, values, "id=?",args);

        return retornoDB;
    }

    public long alterarQtdPartidas(Pessoa p){
        long retornoDB;

        //Classe que pega os conteúdos a serem alterados
        ContentValues values = new ContentValues();

        //informa a coluna e o novo valor
        values.put(PARTIDAS, p.getQtdPartidas());
        values.put(VITORIAS, p.getQtdVitorias());

        String[] args = {String.valueOf(p.getId())};
        retornoDB = getWritableDatabase().update(TABELA, values, "id=?", args);

        return retornoDB;

    }

    public long excluirPessoa(Pessoa p){
        long retornoDB;

        String[] args = {String.valueOf(p.getId())};
        retornoDB = getWritableDatabase().delete(TABELA, "id=?",args);

        return retornoDB;
    }

    public ArrayList<Pessoa> selectAllPessoa(){
        String[] coluns = {ID, NOME, PARTIDAS, VITORIAS, TEMPO_JOGO};
        Cursor cursor = getWritableDatabase().query(TABELA, coluns, null,null, null, null, "upper(Vitorias)", null);

        ArrayList<Pessoa> listPessoa = new ArrayList<>();

        while(cursor.moveToNext()){

            Pessoa p = new Pessoa();

            p.setId(cursor.getInt(0));
            p.setNome(cursor.getString(1));
            p.setQtdPartidas(cursor.getInt(2));
            p.setQtdVitorias(cursor.getInt(3));
            p.setHorasJogadas(cursor.getInt(4));

            listPessoa.add(p);
        }

        return listPessoa;
    }


}
