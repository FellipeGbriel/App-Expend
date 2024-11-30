public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> descricao, valor;
    ArrayList<Integer> idTransacao;

    BancoDeDadosHelper bancoDeDadosHelper;

    RvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recuperando o ID do usuário de SharedPreferences
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        bancoDeDadosHelper = new BancoDeDadosHelper(this);
        descricao = new ArrayList<>();
        valor = new ArrayList<>();
        idTransacao = new ArrayList<>();
        recyclerView = findViewById(R.id.rvTransacoes);
        adapter = new RvAdapter(this, valor, descricao);

        TextView tvSaldo = findViewById(R.id.tvSaldo);

        // Usando o userId de SharedPreferences para obter o saldo
        String saldo = bancoDeDadosHelper.getSaldo(userId);

        tvSaldo.setText("R$ " + saldo);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Exibindo os dados
        displayData();

        // Configurando o botão para adicionar transação
        Button buttonLancar = findViewById(R.id.adicionar_button);
        buttonLancar.setOnClickListener((View view) -> {
            Intent intent = new Intent(getApplicationContext(), LancamentosActivity.class);
            startActivity(intent);
        });

        // Configurando o botão para logout
        Button buttonPerfil = findViewById(R.id.perfil_button);
        buttonPerfil.setOnClickListener((View view) -> {
            Intent intent = new Intent(getApplicationContext(), LogoutActivity.class);
            startActivity(intent);
        });
    }

    private void displayData() {

        // Recuperando o ID do usuário de SharedPreferences
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        // Buscando as transações para o usuário
        Cursor cursor = bancoDeDadosHelper.getTransacoesHome(userId);
        if (cursor.getCount() == 0) {

            Toast.makeText(this, "Nenhuma transação encontrada", Toast.LENGTH_SHORT).show();
            return;
        } else {

            // Adicionando as transações aos arrays de descrição e valor
            while (cursor.moveToNext()) {

                descricao.add(cursor.getString(0));
                valor.add(cursor.getString(1));

            }
        }
    }
}
