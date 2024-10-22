const mysql = require('mysql2');

const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'teste'
});

connection.connect((err) => {
  if (err) {
    console.error('Erro ao conectar ao banco de dados:', err);
    return;
  }
  console.log('Conexão ao banco de dados estabelecida!');
  
  connection.query('SHOW TABLES', (err, results) => {
    if (err) {
      console.error('Erro ao executar consulta:', err);
    } else {
      console.log('Tabelas no banco de dados:', results);
    }
    connection.end();
  });
});

module.exports = connection;