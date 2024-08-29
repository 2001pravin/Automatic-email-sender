import React, { useRef, useState } from "react";
import axios from "axios";
import { Editor } from "@tinymce/tinymce-react";

function EmailSender() {
  const editorRef = useRef(null);
  // const log = () => {
  //   if (editorRef.current) {
  //     console.log(editorRef.current.getContent());
  //   }
  // };
  const [subject, setSubject] = useState("");
  const [body, setBody] = useState("");
  const [attachment, setAttachment] = useState(null);
  const [emailExcel, setEmailExcel] = useState(null);
  const [message, setMessage] = useState("");
  const[Error, setError]=useState("");

  const handleFileChange = (e) => {
    setEmailExcel(e.target.files[0]);
  };

  const handleAttachmentChange = (e) => {
    setAttachment(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const plainTextBody = editorRef.current.getContent({ format: "text" });

    const formData = new FormData();
    formData.append("subject", subject);
    formData.append("body", plainTextBody);
    formData.append("attachment", attachment);
    formData.append("emailExcel", emailExcel);

    try {
      const response = await axios.post(
        "http://localhost:8081/api/email/send",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      setMessage("Email sent successfully!");
      setError(""); // Clear error message on success

      // Clear form fields
      setSubject("");
      setBody("");
      setAttachment(null);
      setEmailExcel(null);
      if (editorRef.current) {
        editorRef.current.setContent(""); // Clear editor content
      }
    } catch (error) {
      setError("Error sending email. Please try again.");
      setMessage(""); // Clear success message on error
    }
  };

  return (
    <form
      className="max-w-sm mx-auto bg-gray-200 rounded-lg"
      onSubmit={handleSubmit}
    >
      <h1 className="mb-5 text-center font-extrabold text-lg text-black p-3 ">
        Email Sender
      </h1>
      <div className="mb-5 ">
        <label
          for="email"
          className="block mb-2 pr-5 pl-3 text-left text-lg font-bold  text-gray-900"
        >
          Subject:
        </label>
        <input
          type="text"
          value={subject}
          onChange={(e) => setSubject(e.target.value)}
          required
          className="bg-gray-50 border border-gray-300 text-black text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-400 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
          placeholder="Add Subject"
        />
      </div>

      <div className="mb-5 ">
        <label
          for="email"
          className="block mb-1/2 pr-5 pl-3  text-left text-lg font-bold text-gray-900 "
        >
          Body:
        </label>
        {/* <textarea 
                    value={body} 
                    onChange={(e) => setBody(e.target.value)} 
                    required 
                /> */}
        {/* <Editor
                 apiKey='rbsrryzajjt6ddxikcfvmnigg63t81swbqjjvczjtdouwpdd'
                 onInit={(_evt, editor) => editorRef.current = editor}
                 initialValue="<p>This is the initial content of the editor.</p>"
                 init={{
                   height: 500,
                   menubar: false,
                   plugins: [
                     'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
                     'anchor', 'searchreplace', 'visualblocks', 'code', 'fullscreen',
                     'insertdatetime', 'media', 'table', 'code', 'help', 'wordcount'
                   ],
                   toolbar: 'undo redo | blocks | ' +
                     'bold italic forecolor | alignleft aligncenter ' +
                     'alignright alignjustify | bullist numlist outdent indent | ' +
                     'removeformat | help',
                   content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }'
                 }}
                
                /> */}
        <Editor
          onEditorChange={(content) => setBody(content)}
          // onEditorChange={(e)=>{
          //     console.log(e);
          //     console.log(editorRef.current.getContent());
          // }}
          onInit={(evt, editor) => {
            editorRef.current = editor;
          }}
          apiKey="rbsrryzajjt6ddxikcfvmnigg63t81swbqjjvczjtdouwpdd"
          initialValue="<p>Write something here...</p>"
          init={{
            plugins:
              "anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount checklist mediaembed casechange export formatpainter pageembed linkchecker a11ychecker tinymcespellchecker permanentpen powerpaste advtable advcode editimage advtemplate ai mentions tinycomments tableofcontents footnotes mergetags autocorrect typography inlinecss markdown",
            toolbar:
              "undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | addcomment showcomments | spellcheckdialog a11ycheck typography | align lineheight | checklist numlist bullist indent outdent | emoticons charmap | removeformat",
            tinycomments_mode: "embedded",
            tinycomments_author: "Author name",
            mergetags_list: [
              { value: "First.Name", title: "First Name" },
              { value: "Email", title: "Email" },
            ],
            ai_request: (request, respondWith) =>
              respondWith.string(() =>
                Promise.reject("See docs to implement AI Assistant")
              ),
            content_style:
              "body { font-family:Helvetica,Arial,sans-serif; font-size:14px; color:#111727FF;; background-color: #9DA3AF; }",
              content_css: [
                'body { background-color: #9DA3AF; }'
              ]          }}
        />
      </div>
      <div>
        <label
          class="block mb-2  pl-3 text-left text-lg font-bold text-gray-900"
          for="Attachment"
        >
          Attachment:
        </label>
        <input
          onChange={handleAttachmentChange}
          class="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:text-black focus:outline-none dark:bg-gray-400 dark:border-gray-600 dark:placeholder-gray-400"
          id="file_input"
          type="file"
        />
      </div>
      <div>
        <label
          class="block mb-2 text-lg pl-3 text-left font-bold text-gray-900"
          for="Email Excel"
        >
          Email Excel
        </label>
        <input
          onChange={handleFileChange}
          accept=".xlsx"
          class="block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 dark:text-black focus:outline-none dark:bg-gray-400 dark:border-gray-600 dark:placeholder-gray-400"
          id="file_input"
          type="file"
        />
      </div>
      <div className="pt-2">
        <button
          className="text-white bg-[#3b5998] hover:bg-[#3b5998]/90 focus:ring-4 focus:outline-none focus:ring-[#3b5998]/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center dark:focus:ring-[#3b5998]/55 me-2 mb-2"
          type="submit"
        >
          Send Email
        </button>
        <button
          className="text-white bg-[#3b5998] hover:bg-[#3b5998]/90 focus:ring-4 focus:outline-none focus:ring-[#3b5998]/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center dark:focus:ring-[#3b5998]/55 me-2 mb-2"
          type="reset"
        >
          Clear
        </button>
      </div>
    </form>
  );
}

export default EmailSender;
