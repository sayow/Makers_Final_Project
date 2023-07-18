object Environment {
    private val env = if (System.getenv("ENVIRONMENT") == "dev") "dev" else "test"

    fun databaseName() = if (env == "dev") "belle_musica" else "belle_musica_test"

    fun port() = if (env == "dev") 9000 else 9999
}