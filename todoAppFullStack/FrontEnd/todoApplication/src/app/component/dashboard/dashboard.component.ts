import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms'
import {Todo} from "../../model/todo";
import {TodoServiceService} from "../../service/todo-service.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  todoDetail !: FormGroup;
  todoObj : Todo = new Todo();
  todoList : Todo[] = [];

  constructor(private formBuilder : FormBuilder, private todoService : TodoServiceService) { }

  ngOnInit(): void {
    this.getAllTodos();

    this.todoDetail = this.formBuilder.group({
      id : [''],
      description : [''],
      summary : ['']
    });
  }
  addTodo(){
    console.log(this.todoDetail);
    this.todoObj.id = this.todoDetail.value.id;
    this.todoObj.description = this.todoDetail.value.description;
    this.todoObj.summary = this.todoDetail.value.summary;

    this.todoService.addTodo(this.todoObj).subscribe(todo => this.todoObj = todo)
    this.getAllTodos();
  }

  getAllTodos(){
    this.todoService.getAllTodos().subscribe(todos => this.todoList = todos)
  }

  editTodo(todo : Todo){
    this.todoDetail.controls['id'].setValue(todo.id)
    this.todoDetail.controls['description'].setValue(todo.description)
    this.todoDetail.controls['summary'].setValue(todo.summary)
  }

  updateTodo(){
    this.todoObj.id = this.todoDetail.value.id;
    this.todoObj.description = this.todoDetail.value.description;
    this.todoObj.summary = this.todoDetail.value.summary;

    this.todoService.updateTodo(this.todoObj).subscribe(todo => this.todoObj = todo)
    return this.getAllTodos();
  }
  deleteTodo(todo : Todo){
    this.todoService.deleteTodo(todo).subscribe(todo => this.todoObj = todo);
  }

}
