# Passkeys Android  Sample App (with Existing Password Support)

This project showcases a sample Android application that implements passkeys using the Credential Manager API, while also maintaining a standard password login option.

## Functionality:

- Users can choose between logging in with traditional passwords or secure passkeys.
- The app demonstrates the integration of the WebAuthn Passkey API for a modern authentication experience.
- Existing password functionality is preserved for compatibility.

## Setup:

- Server URL Update: Modify the server domain within ApiClient.kt. Ensure your server is running with HTTPS.
- App Certificate and Package Update: Update the app certificate fingerprint and package name in your server's .well-known/assetlinks.json file.
- Run the Application: Build and run the app on your Android device.

## Sample assetlinks.json:

```json
[
  {
    "relation": [
      "delegate_permission/common.handle_all_urls",
      "delegate_permission/common.get_login_creds"
    ],
    "target": {
      "namespace": "android_app",
      "package_name":   
 "in.bgaurav.passkeys",
      "sha256_cert_fingerprints": [
        "4F:20:47:1F:D9:9A:BA:96:47:8D:59:27:C2:C8:A6:EA:8E:D2:8D:14:C0:B6:A2:39:99:9F:A3:4D:47:3D:FA:11"   

      ]
    }
  }
]
```

## Additional Resources:

- Server for this App: https://github.com/bgaurav7/passkeys-auth-server (This server is required to run the sample app)
- Simpler Passkey Implementation: https://github.com/bgaurav7/passkeys-auth