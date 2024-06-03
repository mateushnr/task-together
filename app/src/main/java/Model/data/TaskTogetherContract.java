package Model.data;

import android.provider.BaseColumns;

public class TaskTogetherContract {
    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "usuario";

        public static final String COLUMN_NAME = "nome";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE = "telefone";
        public static final String COLUMN_PASSWORD = "senha";
    }

    public static final class GroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "grupo";

        public static final String COLUMN_NAME = "nome";
        public static final String COLUMN_DESCRIPTION = "descricao";
        public static final String COLUMN_TYPE = "tipo";
    }

    public static final class UserGroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "usuario_has_grupo";

        public static final String COLUMN_ID_USER = "idUsuario";
        public static final String COLUMN_ID_GROUP = "idGrupo";

        public static final String COLUMN_ID_WHO_INVITED = "idQuemConvidou";
        public static final String COLUMN_ACCESS_LEVEL = "nivelAcesso";
        public static final String COLUMN_STATUS_PARTICIPATION = "statusParticipation";
    }

    public static final class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tarefa";

        public static final String COLUMN_ID_GROUP = "idGrupo";

        public static final String COLUMN_NAME = "nome";
        public static final String COLUMN_DESCRIPTION = "descricao";
        public static final String COLUMN_STATUS = "situacao";
        public static final String COLUMN_LIMIT_DATE = "dataLimite";
    }

    public static final class UserTaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "usuario_has_task";

        public static final String COLUMN_ID_TASK = "idTask";
        public static final String COLUMN_ID_USER = "idUser";

    }
}