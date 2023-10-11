package com.example.jokempo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jokempo.pessoa.Pessoa;
import com.example.jokempo.pessoaDao.PessoaDao;

public class FormUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_usuario);

        Intent intent = getIntent();
        Pessoa pessoaAlterada = (Pessoa) intent.getSerializableExtra("jogador-alterado");
        Button buttonAcao = findViewById(R.id.buttonAcao);
        TextView textViewAcao = findViewById(R.id.textViewAcao);

        if(pessoaAlterada != null){
            //Significa que existe um jogador e ele deve ser alterado
            buttonAcao.setText("Alterar");
            textViewAcao.setText("Altere seu nick");
        }else{
            buttonAcao.setText("Cadastrar");
            textViewAcao.setText("Faça seu cadastro");
        }
        //Colocar o texto nos botoes e views e chamar as funções quando eles são clicados
        buttonAcao.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                //se existe uma pessoaAlterada, somente deve alterar
                if(pessoaAlterada != null){
                    //Significa que existe um jogador e ele deve ser alterado

                    if(alterarJogador(pessoaAlterada) == 0 ){
                        Toast.makeText(FormUsuario.this,"ALTERAÇÃO FALHOU", Toast.LENGTH_SHORT).show();
                    }else{

                        Toast.makeText(FormUsuario.this,"ALTERAÇÃO REALIZADA", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormUsuario.this, Login.class);

                        startActivity(i);
                    }
                    return;
                }

                //Significa que não existe jogador e está sendo feito um cadastro
                //cadastrarJogador retorna -1 se a operação falhar
                if(cadastrarJogador() == -1){
                    Toast.makeText(FormUsuario.this,"CADASTRO FALHOU", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(FormUsuario.this,"CADASTRO REALIZADO", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(FormUsuario.this, Login.class);

                    startActivity(i);
                }

            }
        });

    }

    public long cadastrarJogador(){

        EditText editNome = findViewById(R.id.editNome);

        String nome = editNome.getText().toString();

        Pessoa novaPessoa = new Pessoa();

        novaPessoa.setNome(nome);
        novaPessoa.setHorasJogadas(0);
        novaPessoa.setQtdVitorias(0);
        novaPessoa.setQtdPartidas(0);

        PessoaDao pessoaDao = new PessoaDao(FormUsuario.this);



        long retorno = pessoaDao.salvarPessoa(novaPessoa);
        pessoaDao.close();

        return retorno;

    }

    public long alterarJogador(Pessoa pessoaAlterada){
        EditText editNome = findViewById(R.id.editNome);

        long retorno;

        String nome = editNome.getText().toString();

        pessoaAlterada.setNome(nome);

        PessoaDao pessoaDao = new PessoaDao(FormUsuario.this);

        retorno = pessoaDao.alterarPessoa(pessoaAlterada);

        //retorno é zero se a operação falhou e não foram atualizados os dados
        //retorno é maior que zero se a operação atualizou dados
        return retorno;
    }
}
