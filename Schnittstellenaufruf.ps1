#Mein Workflow zum Testen: patrick.test


#Die benoetigte Uri (Konstante).
$BASE_URL = "http://localhost:8080/ae/api/v1/executions"

#Der User kann hier seine Daten eingeben, diese werden dann in Variablen abgespeichert.
$Credentials = Get-Credential -Message "Benutzername entspricht Ihrer Novell-Nummer!"
$username = $Credentials.UserName
$Credentials.Password | ConvertFrom-SecureString
$pw = $Credentials.GetNetworkCredential().password

#Die Novell-Nummer im Unternehmen muss noch ergaenzt werden mit folgendem String: /S-V.LOC.
$username = $username + "/S-V.LOC"

#Basic Access Authentication Attribut wird initialisiert mit dem Format Username:Passwort 
#(codiert mit Base64).
$credPair = "$($username):$($pw)"
$encodedCredentials = [System.Convert]::ToBase64String([System.Text.Encoding]::ASCII.GetBytes($credPair))
	
	
#Der User kann hier den Workflow eingeben, welchen er ausfuehren moechte.
$workflowInput = Read-Host -Prompt "Welchen Workflow wollen Sie ausfuehren?"
	
#Das Attribut wird initialisiert, das in dem Body vom HTTP-Protokoll steht, damit die Schnittstelle erkennen kann welcher Workflow
#ausgefuehrt werden soll.
$body = @{
 object_name = $workflowInput
}

#Die benoetigten Parameter fuer die Invoke-RestMethod werden initialisiert (POST).
$parametersForPOST = @{
    Uri         = $BASE_URL
    Headers     = @{ 'Authorization' = "Basic $encodedCredentials" }
    Method      = 'POST'
    Body 				= (ConvertTo-Json $body)
}

#POST-Methode wird ausgefuehrt und der Rueckgabewert wird in der Variable ergebnisPOST abgespeichert. 
#Der Body wurde zuvor noch in Json-Format konvertiert.
Invoke-RestMethod @parametersForPOST