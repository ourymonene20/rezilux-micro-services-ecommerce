<#ftl encoding="utf-8">

<!DOCTYPE html>
    <html lang="fr">
<head>
    <title>Gestion Commande</title>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8"/>
   <#-- <style>
        table {
            border: 1px solid #dddddd;
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 70%;
            margin-left: 15%;
        }

        thead {
            /*border: 1px solid #dddddd;*/
            text-align: center;
            padding: 8px;
            background-color: #dddddd;
        }

        td {
            /*border: 1px solid #dddddd;*/
            text-align: center;
            padding: 8px;
        }

    </style>-->

    <style>
        table{
            width: 70%;
            margin-left: 15%;
        }
        p{
            margin: 0;
        }

        .p1{
            margin-top: 15px;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>


    <table border="1" cellpadding="0" cellspacing="0" align="center">


        <tr>
            <td align="center">
                <img src="https://yupay.rezilux.com:8080/assets/images/logo.png" height="80">
            </td>
        </tr>


        <tr>
            <td height="80">
                <b>Commande N° ${code}</b>
            </td>
        </tr>


        <tr>
            <td>
                <p class="p1">
                    Bonjour ${nom} ${prenom}, <br/><br/>
                    Nous vous remercions d'avoir fait vos achats chez nous. 
                    Votre commande a été confirmée avec succès. 
                    Elle vous sera livrée dans un délai de 48h au plus,tenant compte des jours ouvrables. Pour les autres régions,un délai de 72h est appliqué.
                    Une notification vous sera envoyée à chaque étape du traitement.


                    <br/><br/>
                    <b>Seynabou SAGNE</b> <br/>
                    <b>Marketing / Communication</b> <br/>
                    <b>77 778 19 71</b>
                </p>
            </td>
        </tr>


        <tr>
            <td>
                <p>Destinataire: ${nom} ${prenom}<br>
                    Tel: (+221) ${phone} <br>
                    Adresse ${address} <br> <br>
                </p>
            </td>
        </tr>


        <tr>
            <td>
                <table style="margin-left: 0px">
                    <tr>
                        <th style="padding-right: 5%">Article</th>
                        <th width="50%">Quantité</th>
                        <th>Prix</th>
                    </tr>
                    <#list mapList as mc>
                    <tr align="center">
                        <td><img src="${mc.image}" width="120" height="100">
                            <span style="float: right; margin-top: 40px">${mc.name}</span>
                        </td>
                        <td >${mc.quantity}</td>
                        <td>${mc.price}</td>
                    </tr>
                    </#list>
                </table>
            </td>
        </tr>


        <tr>
            <td>
                <p style="text-align:left; margin-top: 20px">FRAIS LIVARAISON
                    <span style="float:right; margin-right: 50px">CFA 0</span>
                </p><br>
                <p style="text-align:left;">TOTAL
                    <span style="float:right; margin-right: 50px">CFA ${total}</span>
                </p><br>
                <!--<p style="text-align:left;">MODE PAIEMENT
                    <span style="float:right; margin-right: 50px ">
                        <img src="http://yupay.rezilux.com:8080/assets/images/logo.png" width="50" height="50" style="margin-bottom: 0px">
                    </span>
                </p>-->
            </td>
        </tr>


        <tr>
            <td>
                <p>L'équipe YUPAY vous remercie de votre confiance. <br> <br>
                    À bientôt!<br> <br>
                    Téléchager votre application mobile -> Lien: <a href="https://www.rezilux.com">www.rezilux.com</a>
                </p>
            </td>
        </tr>


    </table>

</body>
</html>


<#--lorem picsum(image en ligne)-->

