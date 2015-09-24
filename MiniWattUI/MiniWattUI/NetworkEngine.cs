using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Json.JsonObject;
using System.Json.JsonException;

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
        public void post_question(Object question, QuestionType questionType, Object source, SourceType sourceType, ResponseType responceType)
        {
            JSONObject quesion = new JSONObject();

            try
            {

            } catch(JSONException e)
            {
                e.printStackTrace();
            }
             
            
        }

        public void recieve_answer()
        {

        }
    }
}
