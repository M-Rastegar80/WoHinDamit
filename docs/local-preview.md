# Local API preview

The preview is served by Spring Boot at http://localhost:8080/preview/index.html.
Its HTML, CSS and JavaScript are in src/main/resources/static/preview/.
It uses the same server origin for API requests.

## Requirements

Use a JDK compatible with pom.xml, PostgreSQL running on localhost:5432,
and a database named wohin_damit. The configured database user is postgres.
Run the commands below in PowerShell from this repository directory.
The Maven wrapper downloads dependencies on first use.

## Start the server

Enter your own PostgreSQL password locally at the prompt. Do not put it in
source files or share it in chat.

```powershell
$projectSecret = Read-Host 'PostgreSQL password' -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $projectSecret).Password
try {
    .\mvnw.cmd spring-boot:run
} finally {
    Remove-Item Env:DB_PASSWORD -ErrorAction SilentlyContinue
    $projectSecret = $null
}
```

Keep this terminal open. Wait for Started WoHinDamitApplication.
The current application configuration uses Hibernate ddl-auto=update to
create/update tables. Use a local development database for this preview.

## Optional demo data

After the server has created the tables, open a second PowerShell window in
this repository and run the following. Adjust the PostgreSQL installation
path if needed.

```powershell
& 'C:\Program Files\PostgreSQL\17\bin\psql.exe' -h localhost -p 5432 -U postgres -d wohin_damit -W -v ON_ERROR_STOP=1 -f '.\scripts\seed-demo.sql'
```

The script runs in a transaction. It creates Elektronik (Test), two required
BOOLEAN questions and three category-specific rules. It reuses any existing
recommendations with actions REPAIR, DONATE or RECYCLE without changing them.
If the test category already exists, the script makes no changes; it does
not repair partially missing demo data. Demo recommendations are examples,
not verified guidance or real Vienna location data. The script is imported
manually; the application does not execute it automatically.

## Use and verify

Open http://localhost:8080/preview/index.html, click Verbinden, select
Elektronik (Test), answer both questions and click Empfehlung anzeigen.

| Functional | Repairable | Expected action |
|---|---|---|
| Ja | Ja or Nein | DONATE |
| Nein | Ja | REPAIR |
| Nein | Nein | RECYCLE |

On 12 October 2026, browser screenshots confirmed category/question loading
and the donation result. Direct requests to the running local API confirmed
the repair and recycling results. A user-run Maven test execution reported
55 tests, zero failures and zero errors before the demo data was imported.

To run tests again, use the password-handling block above with
.\mvnw.cmd test instead of .\mvnw.cmd spring-boot:run. Tests use the configured
database; they are not isolated from it by these instructions.

## Scope

The preview loads categories, questions and answer options, submits typed
answers and displays the decision returned by the backend. It handles
loading, empty and error states. All required questions remain visible.

Item search, item-specific decisions, locations and maps are not implemented
in this preview. The original separate frontend has not been integrated.
GitHub publication and deployment are separate steps.
