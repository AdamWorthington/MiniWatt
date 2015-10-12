using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Http;
using System.IO;
using Newtonsoft.Json.Linq;

namespace MiniWattUI
{
    enum QuestionType
    {
        TEXT,
        PDF,
        JPG
    };

    enum SourceType
    {
        TEXT,
        PDF,
        JPG,
        NULL
    };

    enum ResponseType
    {
        TEXT,
        PDF
    };

    class NetworkEngine
    {
        String requestUrl = "This is a filler";
        HttpClient client = new HttpClient();

        public string Base64Encode(FileStream fs)
        {
            MemoryStream ms = new MemoryStream();
            fs.CopyTo(ms);
            byte[] bytes = ms.ToArray();
            ms.Dispose();

            return Convert.ToBase64String(bytes);
        }

        public void post_question(Object question, QuestionType questionType, Object source, SourceType sourceType, ResponseType responseType)
        {
            JObject questionRequest = new JObject();
            
            try
            {
                questionRequest.Add(question);
                questionRequest.Add(questionType);
                questionRequest.Add(source);
                questionRequest.Add(sourceType);
                questionRequest.Add(responseType);
            }
            catch (KeyNotFoundException k)
            {
                // output qustionRequest error
            }


            // command to Post task

            
           

        }

        public void recieve_answer()
        {
            //get task (requestUrl, client);

            JObject answerResponse = null;

            try
            {
                //answerResponse = 
            }
            catch (ArgumentNullException)
            { }

            if (answerResponse == null)
            {
                //Server response error
            }

            try
            {
                // send results to be shown
            }
            catch (ArgumentNullException)
            { }

        }
    }
}
