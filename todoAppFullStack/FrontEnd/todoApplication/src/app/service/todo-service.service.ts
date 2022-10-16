import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHeaders } from "@angular/common/http";
import {Observable, of, tap} from "rxjs";
import {Todo} from "../model/todo";

@Injectable({
  providedIn: 'root'
})

export class TodoServiceService {

  todoAPI : string;

  constructor(private http : HttpClient) {
    this.todoAPI = 'http://localhost:8080/todos'
  }
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  addTodo(todo : Todo): Observable<Todo>{
    return this.http.post<Todo>(this.todoAPI, todo)
  }

  getAllTodos(): Observable<Todo[]>{
    return this.http.get<Todo[]>(this.todoAPI);
  }

  updateTodo(todo : Todo) : Observable<Todo>{
    return this.http.put<Todo>(this.todoAPI+'/' + todo.id, todo);
  }
  deleteTodo(todo : Todo) : Observable<Todo>{
    return this.http.delete<Todo>(this.todoAPI+'/'+todo.id);
  }
}
