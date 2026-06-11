$ErrorActionPreference = 'Stop'

$body = @{ username='admin'; password='admin123' } | ConvertTo-Json
$r = Invoke-RestMethod -Uri 'http://127.0.0.1:8080/api/auth/login' -Method Post -ContentType 'application/json' -Body $body
$token = $r.accessToken
Write-Output '---ACCESS TOKEN---'
Write-Output $token

$pred = @{ homeTeam='Argentina'; awayTeam='Brazil' } | ConvertTo-Json
$resp = Invoke-RestMethod -Uri 'http://127.0.0.1:8080/api/matches/predict' -Method Post -ContentType 'application/json' -Body $pred -Headers @{ Authorization = "Bearer $token" }
Write-Output '---PREDICTION RESPONSE---'
$resp | ConvertTo-Json -Depth 5
