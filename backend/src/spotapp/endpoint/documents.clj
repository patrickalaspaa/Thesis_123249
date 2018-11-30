(ns spotapp.endpoint.documents
  (:require
   [clojure.data.json         :as json]
   [clojure.java.io           :as io]
   [clojure.string            :as string]
   [spotapp.constants         :as const]
   [spotapp.db.documents-sql  :as sql]
   [spotapp.db.connection     :as con :refer [launch-query launch-insert-or-update get-id]]
   [spotapp.utility.utility   :as util :refer [not-nil?]]
   [spotapp.state             :refer [config environment]]))

(def invalid-object
  {:status 400
   :body (json/write-str {const/object-type const/invalid})
   :header {}})

(def file-exists
  {:status 409
   :body (json/write-str {const/object const/exists})
   :header {}})

(defn return-id [id file-name extension]
  {:status 200
   :body (json/write-str {const/id id const/nname file-name const/extension extension})
   :header {}})

(defn add-image [id file-name is file extension]
  (with-open [in (io/input-stream is)
              out (io/output-stream file)]
    (io/copy in out))
  (return-id id file-name extension))

(defn handle-is [file-name extension is]
  (def id (System/currentTimeMillis))
  (def file (str (const/temp-path (get @environment :is-production)) id extension))
  (if (not (.exists (io/as-file file)))
    (add-image id file-name is file extension)
    file-exists))

(defn add-picture [data]
  (def spot-photo (get-in data [:multipart-params const/spot-photo]))
  (if (and (not-nil? spot-photo) (> (get spot-photo :size) 0))
    (do
      (def file-name (get spot-photo :filename))
      (def is (get spot-photo :tempfile))
      (def extension (str "." (second (string/split (spot-photo :content-type) #"/"))))
      (handle-is file-name extension is))
    invalid-object))

(defn image-handler [item folder spot-id person-id]
  (def tmp-file (str const/temp-path (get item const/id) (get item const/extension)))
  (def img-name (get item const/nname))
  (def final
    (if (.exists (io/as-file (str folder img-name)))
      (str folder (System/currentTimeMillis) const/dash img-name)
      (str folder img-name)))
  (io/copy
   (io/file tmp-file)
   (io/file final))
  (launch-insert-or-update (sql/insert-image (get-id) spot-id img-name person-id)))


(defn remove-from-tmp [file]
  (io/delete-file (str const/temp-path (get file const/id) (get file const/extension))))

(defn handle-tmp-images [id data person-id]
  (def folder (str (const/perm-path (get @environment :is-production)) id const/slash))
  (if (not (.exists (io/file folder)))
    (.mkdir (io/file folder)))

  (doseq [item data]
    (image-handler item folder id person-id)
    (remove-from-tmp item)))

(defn get-spot-photos [data]
  (def id (get-in data [:route-params :id]))
  (def nginx-url (get-in @config [:nginx :url]))
  (def baseurl (str nginx-url id const/slash))

  (defn add-img-name [item]
    {
     :id (get item :id)
     :url (str baseurl (get item :name))})
  (def image-names (launch-query (sql/get-all-spot-images id)))
  (def url-list (map add-img-name image-names))
  {:status 200
   :body (json/write-str url-list)
   :header {}})
