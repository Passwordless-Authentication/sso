<#import "template.ftl" as layout>
<@layout.registrationLayout displayMessage=true; section>
    <#if section = "header">
        <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs@7.0.0/bundles/stomp.umd.min.js"></script>
        <script type="text/javascript">
            window.addEventListener('load', () => {
                const stompClient = new StompJs.Client({
                    brokerURL: 'ws://localhost:5000/test'
                });

                stompClient.onConnect = (frame) => {
                    console.log('Connected: ' + frame);
                    stompClient.subscribe('/topic/reload', () => {
                        location.reload()
                    });
                };

                stompClient.onWebSocketError = (error) => {
                    console.error('Error with websocket', error);
                };

                stompClient.onStompError = (frame) => {
                    console.error('Broker reported error: ' + frame.headers['message']);
                    console.error('Additional details: ' + frame.body);
                };

                stompClient.activate();
            })
        </script>
    </#if>

    <#if section = "form">
        <div>
            <strong>Check your Authenticator app</strong>
        </div>

        <div>
            <p><strong>${challengeResponse}</strong></p>
        </div>

        <div>Select this number in the sign-in request on your mobile device</div>
        <div>
            Didn't receive the notification?
            <form action="${url.loginAction}" method="post">
                <button name=resend value="true">resend</button>
            </form>
        </div>
    </#if>
</@layout.registrationLayout>
