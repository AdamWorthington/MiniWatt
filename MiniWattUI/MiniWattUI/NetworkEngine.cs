﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Http;
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

        public void post_question(Object question, QuestionType questionType, Object source, SourceType sourceType, ResponseType responseType)
        {
            JObject questionRequest = new JObject();

            try
            {
                questionRequest.Add(question);
            }
            catch (KeyNotFoundException k)
            {
            }

            try
            {
                questionRequest.Add(questionType);
            }
            catch (KeyNotFoundException k)
            {
            }

            try
            {
                questionRequest.Add(source);
            }
            catch (KeyNotFoundException k)
            {
            }

            try
            {
                questionRequest.Add(sourceType);
            }
            catch (KeyNotFoundException k)
            {
            }

            try
            {
                questionRequest.Add(responseType);
            }
            catch (KeyNotFoundException k)
            {
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
