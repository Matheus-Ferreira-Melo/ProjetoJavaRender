package com.example.apiestoque.controllers;

import com.example.apiestoque.models.Produto;
import com.example.apiestoque.repository.ProdutoRepository;
import com.example.apiestoque.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.hibernate.annotations.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
@Validated
public class ProdutoController {
    private final  ProdutoService produtoService;
    private final Validator validador;

    public ProdutoController(Validator validador, ProdutoService produtoService){

        this.produtoService = produtoService;
        this.validador = validador;
    }
    @GetMapping("/selecionar")
    @Operation(summary = "Selecionar todos os produtos",
                description = "Selecionar todos os produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos os produtos selecionados",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)))
    })
    public ResponseEntity<List<Produto>> listarProdutos(){
        List<Produto> listaProdutos = produtoService.buscarTodosOsProdutos();
        return ResponseEntity.ok(listaProdutos);
    }


    //Post traz a requisição do formulario
    @PostMapping("/inserir")//endPOint
    @Operation(summary = "Inseririr um novo produto", description = "Inseririr um novo produto ao sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto inserido com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)))
    })
    public ResponseEntity<?> /*a resposta sera no formato de String*/inserirProduto(@Valid @RequestBody /*transforma o JSON num objeto*/Produto produto){
        produtoService.salvarProduto(produto);//pega o objeto
        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/excluir/{id}")
    @Operation(summary = "Excluir um produto", description = "remover um produto do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto excluído com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)))
    })
    public ResponseEntity<String> excluirProduto(@Parameter(description = "ID do produto") @Valid @PathVariable Long id) {

//        if (!produtoRepository.existsById(id)){
//            return  ResponseEntity.status(404).body("Produto não encontrado");
//        }else{
//            produtoRepository.deleteById(id);
//            return ResponseEntity.ok("Produto excluído com sucesso");
//        }

        Produto produto = produtoService.buscarProdutoPorID(id);
        if (produto != null){
            produtoService.excluirProduto(id);
            return ResponseEntity.ok("deu certo");
        }else {
            return  ResponseEntity.ok("deu ruim");
        }


    }
    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar um produto", description = "Atualiza um produto ja existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)))
    })
    public ResponseEntity<String> atualizarProduto(@Parameter(description = "ID do produto")    @Valid @PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        Produto produtoExistente = produtoService.buscarProdutoPorID(id);
        if (produtoExistente != null) {
            Produto produto = produtoExistente;
            produto.setNome(produtoAtualizado.getNome());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setQuantidadeestoque(produtoAtualizado.getQuantidadeestoque());
            produtoService.salvarProduto(produto);
            return ResponseEntity.ok("Produto atualizado com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PatchMapping("/atualizarParcial/{id}")
    @Operation(summary = "Atualizar parcialmente um produto", description = "Atualiza uma parte do produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Erro interno do servidor",
                    content = @Content)
    })
    public  ResponseEntity<?> atualizarProdutoParcial(@Parameter(description = "ID do produto") @PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto com as novas informações",content = @Content(schema = @Schema(type = "object",example = "{\"nome\": \"Novonome\","+"\"descricao\": \"Novadescricao\",\"preco\":10.0,\"quantidadeEstoque\": 100}")) ) @RequestBody Map<String, Object> updates) {
        try{
                Produto produto = produtoService.buscarProdutoPorID(id);
                if (updates.containsKey("nome")) {
                    produto.setNome((String) updates.get("nome"));
                }

                if (updates.containsKey("descricao")) {
                    produto.setDescricao((String) updates.get("descricao"));
                }
                if (updates.containsKey("preco")) {

                    produto.setPreco(Double.parseDouble(String.valueOf(updates.get("preco"))));
                }
                if (updates.containsKey("quantidadeEstoque")) {
                    produto.setQuantidadeestoque((Integer) updates.get("quantidadeEstoque"));
                }
                //validar
                DataBinder binder = new DataBinder(produto);
                binder.setValidator(validador);
                binder.validate();
                BindingResult result = binder.getBindingResult();
                if (result.hasErrors()){
                    Map erros = handlerValidator(result);
                    return ResponseEntity.badRequest().body(erros);
                }
                Produto produtosalvo = produtoService.salvarProduto(produto);
                return ResponseEntity.ok("Produto atualizado parcialmente com sucesso");
        }catch (RuntimeException re){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("Produto com ID " + id + " não encontrado");
        }
    }

    @GetMapping("/buscarPorNome")
    @Operation(summary = "Buscar um novo produto pelo nome", description = "Busca o produto pelo nome e mostra seus dados ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos os produtos selecionados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)))
    })
    public ResponseEntity<?> buscarPorNome(@Parameter(description = "Nome do produto") @RequestParam String nome){
        List<Produto> listaProdutos = produtoService.buscarPorNome(nome);
        if (!listaProdutos.isEmpty()){
            return ResponseEntity.ok(listaProdutos);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
    }
    @GetMapping("/buscarPorPrecoMenorIgual")
    @Operation(summary = "Buscar produto pelo preço menor ou igual", description = "Busca produtos pelo preço sendo menor ou igual e mostra seus dados ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos os produtos selecionados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)))
    })
    public ResponseEntity<?> buscarPorPrecoMenorIgual(@Parameter(description = "Preço do produto") @RequestParam double preco){
        List<Produto> listaProdutos = produtoService.buscarPorPrecoMenorIgual(preco);
        if (!listaProdutos.isEmpty()){
            return ResponseEntity.ok(listaProdutos);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
    }
    @GetMapping("/buscarPorPrecoMaiorIgual")
    @Operation(summary = "Buscar produto pelo preço maior ou igual", description = "Busca produtos pelo preço sendo maior ou igual e mostra seus dados ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos os produtos selecionados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)))
    })
    public ResponseEntity<?> buscarPorPrecoMaiorIgual(@Parameter(description = "Preço do produto") @RequestParam double preco){
        List<Produto> listaProdutos = produtoService.buscarPorPrecoMaiorIgual(preco);
        if (!listaProdutos.isEmpty()){
            return ResponseEntity.ok(listaProdutos);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handlerValidator(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}
