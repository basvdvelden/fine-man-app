package nl.management.finance.app.data.api;

import android.os.Message;
import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import okio.Buffer;
import nl.management.finance.app.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RaboHeaderInterceptor implements Interceptor {
    private static final String TAG = "RaboHeaderInterceptor";

    public RaboHeaderInterceptor() {

    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        if (request.url().toString().contains(BuildConfig.RABO_API_URL) && !request.url().toString().contains("/token")) {
            Date date = Calendar.getInstance().getTime();

            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateString = format.format(date);

            String digest = getDigest(request);

            String requestId = UUID.randomUUID().toString();

            request = request.newBuilder()
                    .addHeader("TPP-Signature-Certificate", BuildConfig.RABO_TPP_SC)
                    .addHeader("X-Request-ID", requestId)
                    .addHeader("digest", digest)
                    .addHeader("date", dateString)
                    .addHeader("x-ibm-client-id", BuildConfig.RABO_CLIENT_ID)
                    .build();
            if (request.url().toString().contains("payment-initiation")) {
                request = request.newBuilder()
                        .addHeader("signature", getSignatureHeader(requestId, digest, dateString, request.header("TPP-Redirect-URI")))
                        .build();
            } else {
                request = request.newBuilder()
                        .addHeader("signature", getSignatureHeader(requestId, digest, dateString))
                        .build();
            }
            Log.d(TAG, request.headers().toString());
        }
        return chain.proceed(request);
    }

    private String getDigest(Request request) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digestBytes = md.digest(getRequestBody(request).getBytes());

            return "sha-512=" + Base64.encodeToString(digestBytes, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "no such algorithm for message digest", e);
        }
        return "";
    }

    private String getSignatureHeader(String requestId, String digest, String dateString) {
        String signingString = String.format("date: %s\ndigest: %s\nx-request-id: %s",
                dateString, digest, requestId);
        PrivateKey privateKey = getPrivateKey();
        String stringSignature = "";
        try {
            Signature signature = Signature.getInstance("SHA512withRSA");
            signature.initSign(privateKey);
            signature.update(signingString.getBytes());
            byte[] signatureBytes = signature.sign();

            stringSignature = Base64.encodeToString(signatureBytes, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "signature not found ", e);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "initialized sign with invalid key ", e);
        } catch (SignatureException e) {
            Log.e(TAG, "signature exception thrown ", e);
        }
        return String.format(Locale.forLanguageTag("nl"),
                "keyId=\"%d\",algorithm=\"rsa-sha512\",headers=\"date digest x-request-id\",signature=\"%s\"",
                BuildConfig.RABO_KEY_ID, stringSignature);
    }

    private String getSignatureHeader(String requestId, String digest, String dateString, String redirectUri) {
        String signingString = String.format("date: %s\ndigest: %s\nx-request-id: %s\ntpp-redirect-uri: %s",
                dateString, digest, requestId, redirectUri);
        PrivateKey privateKey = getPrivateKey();
        String stringSignature = "";
        try {
            Signature signature = Signature.getInstance("SHA512withRSA");
            signature.initSign(privateKey);
            signature.update(signingString.getBytes());
            byte[] signatureBytes = signature.sign();

            stringSignature = Base64.encodeToString(signatureBytes, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "signature not found ", e);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "initialized sign with invalid key ", e);
        } catch (SignatureException e) {
            Log.e(TAG, "signature exception thrown ", e);
        }
        return String.format(Locale.forLanguageTag("nl"),
                "keyId=\"%d\",algorithm=\"rsa-sha512\",headers=\"date digest x-request-id tpp-redirect-uri\",signature=\"%s\"",
                BuildConfig.RABO_KEY_ID, stringSignature);
    }

    private PrivateKey getPrivateKey() {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            byte[] encoded = Base64.decode(BuildConfig.RABO_PRIVATE_KEY.getBytes(), Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "no key factory for algorithm ", e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "invalid key spec");
        }
        return null;
    }

    private String getRequestBody(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            Log.e(TAG, "Could not write req body to buffer ", e);
            return "";
        } catch (NullPointerException ignored) {
            return "";
        }
    }
}
