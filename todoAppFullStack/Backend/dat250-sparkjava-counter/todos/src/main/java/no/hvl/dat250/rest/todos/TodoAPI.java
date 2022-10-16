package no.hvl.dat250.rest.todos;

import com.google.gson.Gson;
import java.util.ArrayList;

import static spark.Spark.*;

/**
 * Rest-Endpoint.
 */
public class TodoAPI {
    // Store all todos in a list
    static ArrayList<Todo> todos;
    static int counter;

    private static Todo getTodo(Long todoId) {
        Todo todo = todos.stream()
                .filter(t -> todoId.equals(t.getId()))
                .findAny()
                .orElse(null);
        return todo;
    }

    private static Boolean isLong(String num) {
        try {
            Long.parseLong(num);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        todos = new ArrayList<>();
        counter = 0;

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        after((req, res) -> {
            res.type("application/json");
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        });


        //CREATE (POST)
        post("/todos", (req, res) -> {
            String todoJson = "{id:"+counter+",\n"+req.body().substring(1);
            counter+=1;
            
            Gson gson = new Gson();
            Todo newTodo = gson.fromJson(todoJson, Todo.class);
            todos.add(newTodo);

            Todo nnTodo = getTodo(newTodo.getId());
            return nnTodo.toJson();
        });

        //GET (READ)
        get("/todos", (req, res) -> {
            Gson gson = new Gson();
            return gson.toJson(todos);
        });

        get("/todos/:id", (req,res) ->{
            String inputID = req.params(":id");
            if (!(isLong(inputID))){
                return (String.format("The id \"%s\" is not a number!", inputID));
            }
            Long id = Long.parseLong(inputID);
            Todo gTodo = getTodo(id);
            if (gTodo != null){
                Gson gson = new Gson();
                return gson.toJson(gTodo);
            } else {
                return (String.format("Todo with the id \"%s\" not found!", inputID));
            }
        });

        //UPDATE (PUT)
        put("/todos/:id", (req,res) -> {
            String inputID = req.params(":id");

            if(!isLong(inputID)){
                return (String.format("The id \"%s\" is not a number!", inputID));
            }
            Todo uTodo = getTodo(Long.parseLong(inputID));
            if(uTodo != null){
                todos.remove(uTodo);
            }

            Gson gson = new Gson();
            Todo newTodo = gson.fromJson(req.body(), Todo.class);
            todos.add(newTodo);
            return gson.toJson(newTodo);
        });

        //DELETE (DELETE)
        delete("/todos/:id", (req,res) -> {
            String inputID = req.params(":id");
            if (!(isLong(inputID))){
                return (String.format("The id \"%s\" is not a number!", inputID));
            }
            Todo deletedTodo = getTodo(Long.parseLong(inputID));
            todos.remove(deletedTodo);
            if (deletedTodo != null){
                todos.remove(deletedTodo);
            } else {
                return (String.format("Todo with the id \"%s\" not found!", inputID));
            }
            Gson gson = new Gson();
            return gson.toJson(deletedTodo);
        });
    }
}
