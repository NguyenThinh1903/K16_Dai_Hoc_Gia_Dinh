// Import required modules
const express = require('express');
const bodyParser = require('body-parser');
const { log } = require('console');

// Initialize the app and middleware
const app = express();
app.use(bodyParser.json());

// Sample in-memory data store for to-do tasks
let todos = [
    { id: 1, task: 'Learn Node.js', completed: false },
    { id: 2, task: 'Build a REST API', completed: false },
    { id: 3, task: 'Write unit tests', completed: false },
    { id: 4, task: 'Deploy application', completed: true },
    { id: 5, task: 'Document API endpoints', completed: false }
];
app.set('view engine', 'pug');
app.use(bodyParser.urlencoded({ extended: true }));

// Get all to-do tasks

app.get('/todos', (req, res) => {
    res.render('todos', { todos });
});

// Get all to-do tasks (JSON)
app.get('/todos/json', (req, res) => {
    res.json(todos);
});

// Add a new task
app.post('/todos', (req, res) => {
    const { task } = req.body; 
    if (task) {
        const newTodo = {
            id: todos.length + 1,
            task,
            completed: false 
        };
        todos.push(newTodo); 
        res.redirect('/todos'); 
    } else {
        res.status(400).send('Task is required');
    }
});


// Update a task
app.put('/todos/:id', (req, res) => {
    const id = parseInt(req.params.id, 10);
    const { task } = req.body;
    const todo = todos.find(t => t.id === id);
    if (todo) {
        todo.task = task;
        res.json({ message: 'Task updated successfully' });
    } else {
        res.status(404).json({ error: 'Task not found' });
    }
});

// Delete a task
app.delete('/todos/:id', (req, res) => {
    const id = parseInt(req.params.id, 10);
    const index = todos.findIndex(t => t.id === id);
    if (index !== -1) {
        todos.splice(index, 1);
        res.json({ message: 'Task deleted successfully' });
    } else {
        res.status(404).json({ error: 'Task not found' });
    }
});


app.listen(3000, () => {
    console.log('Server is listening on port 3000');
});
  