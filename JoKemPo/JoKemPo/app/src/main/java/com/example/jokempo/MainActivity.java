package com.example.jokempo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jokempo.pessoa.Pessoa;
import com.example.jokempo.pessoaDao.PessoaDao;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Pessoa jogador;
    long tempoInicio;
    long tempoFim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        tempoInicio = SystemClock.uptimeMillis();
        /*
         * TO DO:
         * [X] PEGAR O PLAYER
         * [] A CADA RODADA ATUALIZAR QTD VITORIAS E QTD DE PARTIDAS
         * [x] PERMITIR MUDAR O NOME QUANDO CLICAR EM ALTERAR
         * [] SABER QTD DE HORAS JOGADAS
         * */
        jogador = (Pessoa) intent.getSerializableExtra("jogador-enviado");

        String nome = jogador.getNome();


        //declarando os objetos
        Button buttonAlterar = findViewById(R.id.buttonAlterar);
        TextView textViewNome = findViewById(R.id.textViewJogador);

        //define o nome
        String texto = "Jogador: " + nome;
        textViewNome.setText(texto);

        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FormUsuario.class);
                i.putExtra("jogador-alterado", jogador);
                startActivity(i);
            }
        });
    }

    //Métodos para acionar os botões.
    //View referecia os componentes de interface
    public void pedraSelecionada(View view) {
        this.itemSelecionado("pedra");
        Toast.makeText(this, "Pedra selecionada.", Toast.LENGTH_SHORT).show(); //Toast mensagem na tela do usario
    }

    public void papelSelecionado(View view) {
        this.itemSelecionado("papel");
        Toast.makeText(this, "Papel selecionado.", Toast.LENGTH_SHORT).show(); //Toast mensagem na tela do usario
    }

    public void tesouraSelecionada(View view) {
        this.itemSelecionado("tesoura");
        Toast.makeText(this, "Tesoura selecionada.", Toast.LENGTH_SHORT).show(); //Toast mensagem na tela do usario
    }

    public int itemSelecionado(String itemSelecionado) { //liga as imagens aos étodos de acionamento dos botões

        ImageView imageResult = findViewById(R.id.imageResult);
        TextView textResult = findViewById(R.id.textResult);


        int item = new Random().nextInt(3); // elementos de escolha
        String[] escolhas = {"pedra", "papel", "tesoura"}; // arrey para encontrar os elementos disponiveis no joguinho
        String escolhaAdv = escolhas[item];



        //Gerar as imagens resultantes randomicamente
        if (item == 0) {
            imageResult.setImageResource(R.drawable.pedra);
        } else if (item == 1) {
            imageResult.setImageResource(R.drawable.papel);
        } else if (item == 2) {
            imageResult.setImageResource(R.drawable.tesoura);
        } else {
            imageResult.setImageResource(R.drawable.padrao);
        }


        int qtdVitorias = jogador.getQtdVitorias();
        int qtdPartidas = jogador.getQtdPartidas() + 1;
        jogador.setQtdPartidas(qtdPartidas);
        // if's para informar o resultado do jogo, possiveis situações do jogo
        if ((escolhaAdv.equals("pedra") && itemSelecionado.equals("tesoura")) ||
                (escolhaAdv.equals("papel") && itemSelecionado.equals("pedra")) ||
                (escolhaAdv.equals("tesoura") && itemSelecionado.equals("papel"))) {

            textResult.setText("Você perdeu!");

        } else if ((itemSelecionado.equals("pedra") && escolhaAdv.equals("tesoura")) ||
                (itemSelecionado.equals("papel") && escolhaAdv.equals("pedra")) ||
                (itemSelecionado.equals("tesoura") && escolhaAdv.equals("papel"))){
            textResult.setText("Você Ganhou!");

            qtdVitorias += 1;


        } else {
            textResult.setText("Empate!");
        }

        float taxaVitoria = Float.parseFloat(String.valueOf(qtdVitorias))/qtdPartidas;

        jogador.setQtdPartidas(qtdPartidas);
        jogador.setQtdVitorias(qtdVitorias);
        jogador.setTaxaVitorias(taxaVitoria);

        PessoaDao pessoaDao = new PessoaDao(MainActivity.this);
        pessoaDao.alterarQtdPartidas(jogador);
        pessoaDao.close();

        return item;
    }

    @Override
    protected void onStop() {

        tempoFim = SystemClock.uptimeMillis();
        double horas = (double) tempoFim - tempoInicio;

        horas = (horas/1000.00) / 3600.00;
        jogador.setHorasJogadas(horas);

        PessoaDao pessoaDao = new PessoaDao(this);
        pessoaDao.alterarHoras(jogador);

        super.onStop();
    }
}