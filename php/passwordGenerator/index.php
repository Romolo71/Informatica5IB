<?php

$password = "";
$error = "";

if ($_SERVER["REQUEST_METHOD"] === "POST") {

    $length = filter_input(INPUT_POST, "length", FILTER_VALIDATE_INT);

    $uppercase = isset($_POST["uppercase"]);
    $lowercase = isset($_POST["lowercase"]);
    $numbers = isset($_POST["numbers"]);
    $symbols = isset($_POST["symbols"]);

    // Verifica della lunghezza
    if ($length === false || $length < 8 || $length > 32) {
        $error = "La lunghezza deve essere compresa tra 8 e 32 caratteri.";
    }

    // Verifica delle categorie selezionate
    elseif (!$uppercase && !$lowercase && !$numbers && !$symbols) {
        $error = "Seleziona almeno una categoria di caratteri.";
    } else {

        // Definizione delle categorie
        $categories = [];

        if ($uppercase) {
            $categories[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }

        if ($lowercase) {
            $categories[] = "abcdefghijklmnopqrstuvwxyz";
        }

        if ($numbers) {
            $categories[] = "0123456789";
        }

        if ($symbols) {
            $categories[] = "!@#$%^&*()-_=+";
        }

        // Verifica che la lunghezza sia sufficiente
        if ($length < count($categories)) {
            $error = "La lunghezza non è sufficiente per includere tutte le categorie.";
        } else {

            $allCharacters = implode("", $categories);
            $passwordArray = [];

            // Inserisce almeno un carattere per ogni categoria
            foreach ($categories as $category) {

                $index = random_int(0, strlen($category) - 1);

                $passwordArray[] = $category[$index];
            }

            // Completa la password fino alla lunghezza richiesta
            while (count($passwordArray) < $length) {

                $index = random_int(0, strlen($allCharacters) - 1);

                $passwordArray[] = $allCharacters[$index];
            }

            // Mescola i caratteri per evitare un ordine prevedibile
            for ($i = count($passwordArray) - 1; $i > 0; $i--) {

                $j = random_int(0, $i);

                $temp = $passwordArray[$i];

                $passwordArray[$i] = $passwordArray[$j];

                $passwordArray[$j] = $temp;
            }

            // Converte l'array in una stringa
            $password = implode("", $passwordArray);
        }
    }
}

?>

<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Generatore Password</title>

    <link rel="stylesheet" href="style.css">

</head>

<body>

    <div class="container">

        <h1>Generatore Password</h1>

        <form method="POST" action="">

            <label for="length">Lunghezza password:</label>

            <input
                type="number"
                id="length"
                name="length"
                min="8"
                max="32"
                value="<?= htmlspecialchars($_POST['length'] ?? '16') ?>"
                required>

            <h3>Tipologie di caratteri</h3>

            <div class="options">

                <label>
                    <input type="checkbox" name="uppercase"
                        <?= isset($_POST['uppercase']) ? 'checked' : '' ?>>
                    Lettere maiuscole (A-Z)
                </label>

                <label>
                    <input type="checkbox" name="lowercase"
                        <?= isset($_POST['lowercase']) ? 'checked' : '' ?>>
                    Lettere minuscole (a-z)
                </label>

                <label>
                    <input type="checkbox" name="numbers"
                        <?= isset($_POST['numbers']) ? 'checked' : '' ?>>
                    Numeri (0-9)
                </label>

                <label>
                    <input type="checkbox" name="symbols"
                        <?= isset($_POST['symbols']) ? 'checked' : '' ?>>
                    Simboli speciali (!@#$%)
                </label>

            </div>

            <button type="submit">Genera password</button>

        </form>

        <?php if ($error): ?>

            <p class="error">
                <?= htmlspecialchars($error) ?>
            </p>

        <?php endif; ?>

        <?php if ($password): ?>

            <div class="result">

                <h3>Password generata:</h3>

                <div class="password-box">

                    <input
                        type="text"
                        id="password"
                        value="<?= htmlspecialchars($password) ?>"
                        readonly>

                    <button type="button" onclick="copyPassword()">
                        Copia
                    </button>

                </div>

            </div>

        <?php endif; ?>

    </div>

    <script src="script.js"></script>

</body>

</html>
