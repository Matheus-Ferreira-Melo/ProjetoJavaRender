
package com.example.apiestoque.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity

public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do Produto", example = "1234")
    private long id;
    @NotNull(message = "O NOME NÃO PODE SER NULO!!")
    @Size(min = 2, message = "O NOME DEVE TER PELO MENOS 2 CARACTERES")
    @Schema(description = "Nome do Produto", example = "Maca")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Hamburguer artesanal")
    private String descricao;

    @NotNull(message = "O PREÇO NÃO PODE SER NULO")
    @Min(value = 0, message = "O PREÇO DEVE SER PELO MENOS 0")
    @Schema(description = "Preço do Produto", example = "22.50")
    private double preco;

    @NotNull(message = "A QUANTIDADE NÃO PODE SER NULA")
    @Min(value = 0, message = "A QUANTIDADE DESE SER PELO MENOS 0")
    @Schema(description = "Quantidade em estoque do Produto", example = "10")
    private int quantidadeestoque;


    public Produto() {
    }

    public Produto(int id, String nome, String descricao, double preco, int quantidadedeestoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeestoque = quantidadeestoque;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeestoque() {
        return quantidadeestoque;
    }

    public void setQuantidadeestoque(int quantidadeestoque) {
        this.quantidadeestoque = quantidadeestoque;
    }


    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", quantidadedeestoque=" + quantidadeestoque +
                '}';
    }
}
