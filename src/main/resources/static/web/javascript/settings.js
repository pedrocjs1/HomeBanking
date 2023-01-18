const {createApp} = Vue

const settings = createApp({
    data(){
        return {
            username: "",

            emailVerification: "",
            currentPassword: "",
            newPassword: "",


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
            optionBackground:"",
            checkedHeader: true,
            header: "",
            headerDesktop: "",
            shadowCardLoan: "",

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
            .get("http://localhost:8080/api/clients/current")
            .then((data) =>{
                this.cards = data.data.cards.sort((a,b) => a.id - b.id)
                this.username = data.data.firstName + ' ' + data.data.lastName
                // this.clientsAccount = this.accountClient.sort((a,b) => a.id - b.id)
                // this.balanceTotal = this.clientsAccount.map(account => account.balance).reduce((iter, acc) => iter + acc).toFixed(2)
                console.log(this.username)
                
                
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
        logout() {
            axios.post('/api/logout').then(response => {
                
                window.location.href = './index.html'
            })
        },
        changePassword() {
            Swal.fire({
                title: '¿Do you want to change your password??',
                showDenyButton: true,
                confirmButtonText: 'Accept',
                denyButtonText: `Cancel`,
            }).then((result) => {
                
                if (result.isConfirmed) {
                    axios.patch(
                                "/api/clients/current/password",
                                `newPassword=${this.newPassword}&password=${this.currentPassword}&email=${this.emailVerification}`
                            )
                            .then(response => {
                                Swal.fire('Change password Success', '', 'success')
                                    .then(result => {
                                        window.location.href=("./accounts.html")
                                    })
                            }).catch((error) => Swal.fire({
                                icon: "error",
                                title: "Oops...",
                                text: `${error.response.data}`,
                            }));
                }else {
                    Swal.fire('Change password canceled')
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
          this.optionBackground = "optionBackground-black"
          this.shadowCardLoan = "borderCard-white"
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
          this.optionBackground = "optionBackground-white"
          this.shadowCardLoan = "borderCard-black"
          localStorage.setItem("mode", false)
        }
    },
    imageUrl() {
        return `./images/cardImage/${this.selectType}${this.selectColor}.png`
    },
    imageDiscount() {
        return `./images/cardImage/${this.selectType}.png`
    },
    imageDiscountCredit() {
        return `./images/cardImage/${this.selectType}${this.selectColor}1.png`
    },

    
    
    
    }
})


settings.mount('#settings')



