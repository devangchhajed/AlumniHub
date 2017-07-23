package zuts.bit.connect.Activities.HomeActivity.FragmentTwo.uploadActivity;

/**
 * Created by Devang on 1/13/2017.
 */

public class SubjectListModel {
    int id;
    String name, subcode;
    public SubjectListModel(int id, String subcode, String name){
        this.id=id;
        this.name=name;
        this.subcode=subcode;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getSubcode(){
        return this.subcode;
    }
}
