package com.example.apiestoque.services;

import com.example.apiestoque.models.Produto;
import com.example.apiestoque.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;


    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }
    public List<Produto> buscarTodosOsProdutos(){
        return produtoRepository.findAll();
    }
    public Produto salvarProduto(Produto produto){
        return produtoRepository.save(produto);
    }
    public Produto buscarProdutoPorID(Long id){
        return produtoRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Produto não encontardo"));
    }

    public Produto excluirProduto(Long id){
        Optional<Produto> prod = produtoRepository.findById(id);
        if (prod.isPresent()){
            produtoRepository.deleteById(id);
            return prod.get();
        }
        return null;
    }
    public List<Produto> buscarPorNome(String nome){
        return produtoRepository.findByNomeLikeIgnoreCase(nome);
    }

    public List<Produto> buscarPorPrecoMaiorIgual(Double preco){
        return produtoRepository.findByPrecoGreaterThanEqual(preco);
    }

    public List<Produto> buscarPorPrecoMenorIgual(Double preco){
        return produtoRepository.findByPrecoLessThanEqual(preco);
    }


}
