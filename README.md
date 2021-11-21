# URLRedirector

--- 
## Setup

*  Clone the project using `git clone https://github.com/ANutley/URLShortener.git`
*  Enter the URLShortener directory
*  Build the project using ./gradlew shadowJar in the working directory.
*  Go to ./build/libs/ and find the URLShortener-1.0-all.jar file.
*  In the same directory as the jar file, place a config.json file and populate it with the required values (example can be found at https://github.com/ANutley/URLShortener/tree/master/src/main/resources/config.json).
*  Then run the jar file using the command `java -jar URLShortener-1.0-all.jar`

---

## Configuration

### Webserver
* Secret - The authentication key to be used (should be passed as a header, key: secret | value: what you put in
  config.json)
* ExpirationDays - The amount of days that non-permanent urls should last for before being purged (set to -1 to disable)
* URLCodeSize - The size of redirect codes that are generated (eg: u.anutley.me/Sj4Ghn), minimum 6 to avoid duplicate
  codes
* Port - The port Javalin should start with
* Domain - The domain / ip this application will use (used to generate the urls that are passed back to the user)

###ShareX

ShareX isn't required for URLShortener to work, but it allows you to easily send post requests to your webserver.

* Download the .sxcu file from https://i.anutley.me/static/URLShortenerUpload.sxcu
* Open ShareX and navigate to the Destination tab.
* From there click custom uploader and import the previously downloaded .sxcu file
* Change the `secret` header to match the `Secret` config option (as explained above)
* Then configure a keybind, and you are ready to go!
---

## Endpoints

`/GET /api/{code}/` -> Returns JSON data containing the url to forward to, the url code, whether it is a permanent link,
the time created in millis

`/POST /api/` \/
```
{
  //Please use application/json
  //Make sure to set a header of "secret" with a value that matches the "Secret" value in the config.json
  
  "url": "https://url-to-forward.to",
  "permanent": "true/false || t/f",
  "code": "The custom redirect code to use. Will use a random alphanumeric if this is blank/already in use"
}

```

---

