<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true; section>
    <#if section = "form">
        <div>
            <strong>Get a code to sign in</strong>
        </div>

        <div>We'll send a sign-in request to your phone to sign in</div>

        <form action="${url.loginAction}" method="post">
            <button name=submit>Send notification</button>
        </form>
    </#if>
</@layout.registrationLayout>
