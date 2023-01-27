const {createApp} = Vue

const card = createApp({
    data(){
        return {
            cards:[],
            cardsData: [],
            clientsAccount: {},
            loans: [],
            clientLoans: {},
            balanceTotalLoan: "",
            
            
            username: "",
            

            checkedAccount: undefined,
            currentText: "",
            currentBackGround: "",
            mode: "",
            boxShadow: "",
            backgroundBody: "",
            backgroundTitle: "",
            shadowCard: "",
            bgGrey: "",
            backgroundLogo: "",

            checkedHeader: true,
            header: "",
            headerDesktop: "",

            balanceTotal: "",

            view: true,

            viewPassword: "d-none",
            noViewPassword: ""
            
            
            
            
        }
    },
    created(){
        this.loadData()
        this.checkedAccount = localStorage.getItem("mode") 
    },
    methods: {
        loadData(){
            axios
            .get("/api/clients/current")
            .then((data) =>{
                this.cardsData = data.data.cards.sort((a,b) => a.id - b.id)
                this.username = data.data.firstName + ' ' + data.data.lastName
                this.cards = this.cardsData.filter(card => card.disable === false)
                // this.clientsAccount = this.accountClient.sort((a,b) => a.id - b.id)
                // this.balanceTotal = this.clientsAccount.map(account => account.balance).reduce((iter, acc) => iter + acc).toFixed(2)
                console.log(this.cards)
                
                
            })
            .catch((error) => console.log(error))

            
        },
        collapseHeaderMovile(){
            if (this.header == "mob-menu-opened"){
                this.header = ""
            } else {
                this.header = "mob-menu-opened"
            }
        },
        formatedDateCard(dateInput) {
            const date = new Date(dateInput);

            // si mes es menor a 10 le agrego un "0"
            let month = (date.getMonth() + 1) > 9 ? date.getMonth() + 1 : "0" + (date.getMonth() + 1);

            let year = date.getFullYear().toString().slice(-2);
            return month + "/" + year
        },
        viewPasswordFunction(){
            if (this.noViewPassword == "") {
                this.viewPassword = ""
                this.noViewPassword = "d-none"              
            } else if (this.noViewPassword == "d-none") {
                this.noViewPassword = ""
                this.viewPassword = "d-none"
            }
        },
        logout() {
            axios.post('/api/logout').then(response => {
                
                window.location.href = './index.html'
            })
        },
        disabledCard(card) {
            Swal.fire({
                title: '¿Do you want to disable this card??',
                showDenyButton: true,
                text: `Card ${card.type} and color ${card.color}`,
                confirmButtonText: 'Accept',
                denyButtonText: `Cancel`,
            }).then((result) => {
                
                if (result.isConfirmed) {
                    axios.patch(
                                "/api/clients/current/cards",
                                `id=${card.id}`
                            )
                            Swal.fire('Card properly disabled')
                                .then(result => {
                                    window.location.reload()
                                })
        
                            .catch((error) => Swal.fire({
                                icon: "error",
                                title: "Oops...",
                                text: `${error.response.data}`,
                            }));
                }else {
                    Swal.fire('Disabled card canceled')
                                .then(result => {
                                    window.location.reload()
                                })
                }
        
                
            })
        }
        
    
        
       
        
        
            
    },
    computed: {
      darkMode(){
        if (this.checkedAccount === "true" || this.checkedAccount === true) {
          this.currentText = "text-white"
          this.currentBackGround = "bg-dark"
          this.mode = "Dark Mode"
          this.backgroundBody = "backgroundBody"
          this.backgroundTitle = "bg-dark"
          this.shadowCard = "shadowWhite" 
          this.bgGrey = "bg-light borderRadius"
          this.backgroundLogo = ""
          this.shadowCard = "shadowCard-black"
          localStorage.setItem("mode", true)

        } else if (this.checkedAccount === "false" || this.checkedAccount === false) {
          this.currentText = "text-black"
          this.currentBackGround = "backGroundGrey"
          this.mode = "Ligth Mode"
          this.boxShadow = "m-3 shadow bg-body rounded"
          this.backgroundBody = "backgroundWhite "
          this.backgroundTitle = "backgroundTitletarget"
          this.shadowCard = "shadow  bg-body rounded" 
          this.bgGrey = "backgroundBodyCard borderRadius"
          this.backgroundLogo = ""
          this.shadowCard = "shadowCard-light"
          localStorage.setItem("mode", false)
        }
    }

    
    
    
    }
})


card.mount('#card')



