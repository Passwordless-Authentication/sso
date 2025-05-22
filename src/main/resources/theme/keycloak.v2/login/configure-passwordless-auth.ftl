<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true; section>
    <#if section = "form">
        <#assign statusLabel = (passwordlessEnabled)?then(passwordlessEnabledLabel, passwordlessDisabledLabel)>
        <#assign buttonLabel = (passwordlessEnabled)?then(passwordlessDisableButtonLabel, passwordlessEnableButtonLabel)>
        <div>
            <p>Passwordless Authentication is currently <strong>${statusLabel}</strong>.</p>
        </div>
        <form action="${url.loginAction}" method="post">
            <button name=${passwordlessStatus} value=${buttonLabel?lower_case}>${buttonLabel}</button>
        </form>
    </#if>
</@layout.registrationLayout>
