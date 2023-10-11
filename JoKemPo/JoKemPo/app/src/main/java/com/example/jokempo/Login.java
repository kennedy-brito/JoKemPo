package com.example.jokempo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jokempo.pessoa.Pessoa;
import com.example.jokempo.pessoaDao.PessoaDao;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    Pessoa jogador;
    PessoaDao pessoaDao;
    ArrayAdapter<Pessoa> arrayAdapterPessoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Botão que vai para a tela de cadastro
        Button buttonCadastro = findViewById(R.id.buttonCadastro);
        //Lista de jogadores
        ListView listaVisivel = findViewById(R.id.listJogadores);
        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irTelaCadastro();
            }
        });
        //Setando evento que vai para a tela de cadastro
        //TO DO
        /*
        * [x] EXIBIR JOGADORES CADASTRADOS NA LISTA
        *   [x] EXIBIR NOME
        *   [x] HORAS JOGADAS
        *   [x] TAXA DE VITORIAS
        * [X] FAZER O LOGIN CLICANDO EM ITENS DA LISTA
        * [X] FAZER A OPÇÃO DE DELETAR UM JOGADOR AO CLICAR E SEGURAR
        *
        * */

        listaVisivel.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                jogador = (Pessoa) arrayAdapterPessoa.getItem(position);
                Intent i = new Intent(Login.this, MainActivity.class);
                i.putExtra("jogador-enviado", jogador);
                startActivity(i);

            }
        });

        //Não estava aparecendo o menu pois é necessário registra-lo para que ele possa receber um menu
        registerForContextMenu(listaVisivel);
        listaVisivel.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                jogador = arrayAdapterPessoa.getItem(position);

                return false;
            }
        });


    }

    private void irTelaCadastro(){

        Intent intent = new Intent(this, FormUsuario.class);
        startActivity(intent);
    }

    //Função que popula a lista
    public void populaLista() {
        ArrayList<Pessoa> listaPessoa = new ArrayList<>();

        pessoaDao = new PessoaDao(Login.this);
        listaPessoa = pessoaDao.selectAllPessoa();
        pessoaDao.close();

        ListView listaVisivel = findViewById(R.id.listJogadores);

        if (listaPessoa != null) {
            // Crie um adaptador personalizado para exibir os dados da Pessoa
            arrayAdapterPessoa = new ArrayAdapter<Pessoa>(Login.this, R.layout.list_item_layout, R.id.nomeTextView, listaPessoa) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    // Configure os elementos da lista com os dados da Pessoa
                    Pessoa pessoa = getItem(position);
                    TextView nomeTextView = view.findViewById(R.id.nomeTextView);
                    TextView horasJogadasTextView = view.findViewById(R.id.horasJogadasTextView);
                    TextView taxaVitoriasTextView = view.findViewById(R.id.taxaVitoriasTextView);

                    if (pessoa != null) {

                        double qtdPartidas = pessoa.getQtdPartidas();
                        double qtdVitorias = pessoa.getQtdVitorias();

                        double taxaVitorias = qtdVitorias/qtdPartidas;

                        if(qtdPartidas == 0){
                            taxaVitorias = 0;
                        }

                        nomeTextView.setText("Nome: " + pessoa.getNome());
                        horasJogadasTextView.setText("Horas Jogadas: " + pessoa.getHorasJogadas());
                        taxaVitoriasTextView.setText("Taxa de Vitórias: " + taxaVitorias + "%");
                    }

                    return view;
                }
            };

            listaVisivel.setAdapter(arrayAdapterPessoa);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){

        MenuItem mDelete = menu.add("Excluir Jogador");
        mDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {

                long retornoDB;
                pessoaDao = new PessoaDao(Login.this);
                retornoDB = pessoaDao.excluirPessoa(jogador);
                pessoaDao.close();

                if(retornoDB == -1){
                    alert("Erro ao excluir");
                }else{
                    alert("Jogador excluido com sucesso");
                }

                populaLista();
                return false;
            }
        });
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    private void alert(String s){ Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
    @Override
    protected void onResume(){
        super.onResume();
        populaLista();
    }
}