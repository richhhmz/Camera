var users = [
    {"name":"Rich", "guid":"a7e73afb-3dc1-49e6-b2ac-d0aef7401e41"},
    {"name":"Anna", "guid":"e791789c-3e43-4ec6-9e9f-70742685e557"},
    {"name":"Alan", "guid":"2428395b-327b-46ea-a4ed-83e7d693600c"},
    {"name":"Ray", "guid":"6277ad31-d59b-40ad-b264-2e6063d6fc9a"},
    {"name":"Sarah", "guid":"238a165c-6394-4e97-ae0e-d3cbb0c64960"}
];

var authorized = function(guid){
	for(i=0; i<users.length; i++){
		if(guid == users[i].guid) return users[i].name;
	}
	return "";
}
module.exports.authorized = authorized;
