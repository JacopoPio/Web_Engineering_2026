<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Soccorso Web</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .login-box {
            width: 350px;
            margin: 80px auto;
            background: white;
            padding: 25px;
            border-radius: 10px;
        }

        h2 {
            text-align: center;
            color: #d32f2f;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            margin-top: 20px;
            padding: 12px;
            background: #d32f2f;
            color: white;
            font-size: 16px;
            border: none;
            cursor: pointer;
        }

        .errore {
            color: red;
            margin-top: 10px;
            text-align: center;
        }
    </style>
</head>
<body>

    <div class="login-box">
        <h2>Login</h2>

        <#if errore??>
            <p class="errore">${errore}</p>
        </#if>

        <form action="login" method="post">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Accedi</button>
        </form>
    </div>

</body>
</html>