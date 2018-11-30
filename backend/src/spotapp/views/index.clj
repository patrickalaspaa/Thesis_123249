(ns spotapp.views.index)

(def indexpage
  (str
    "<!DOCTYPE html>
    <html>
     <head>
       <meta charset=\"UTF-8\">
       <title>HFL</title>
     </head>
     <body>
      <h2>HFL</h2>
      <p>Welcome to the HFL API indexpage</p>
      <h3>Available endpoints</h3>
      <p>/person (GET, POST)</p>
      <p>/person/{id} (GET)</p>
      <p>/spot (GET, POST)</p>
      <p>/spot/{id} (GET)</p>
     </body>
    </html>"))
